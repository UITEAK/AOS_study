#include<stdio.h>
#include<math.h>

#define TRUE 1
#define FALSE 0
#define ON 1
#define OFF 0
#define ON_CHAR '1'
#define OFF_CHAR '0'
#define UNKNOWN '?'

#define ETHA 0.5
#define BETHA 0.5
#define ALPHA 0.2

#define MAX_NEURONS 101
#define MAX_TRAIN 20
#define DIFF_MIN 0.1
#define MAX_LOOP 100000
#define RAND(min, max) ((rand()-16383.5)/16383.5)*(max-min)+min

#define f(x) ((x<-10.0)?0.0:((x>10.)?1:1.0/(1.0+exp(-x))))
#define abs(x) ((x<0)? -x:x)
#define CHAR(x) ((x==ON)?ON_CHAR:((x==OFF)?OFF_CHAR:UNKNOWN))
#define value(x) ((x==ON_CHAR)?ON:OFF)

typedef struct	
{
	int n_input;
	int n_hidden;
	int n_output;
	
	float *i_value;
	float h_value[MAX_NEURONS];
	float o_value[MAX_NEURONS];

	float i_to_h[MAX_NEURONS][MAX_NEURONS];
	float h_to_o[MAX_NEURONS][MAX_NEURONS];

	float h_offset[MAX_NEURONS];
	float o_offset[MAX_NEURONS];

}NN_str;

typedef struct
{
    float i_value[MAX_NEURONS];
    float o_value[MAX_NEURONS];
}T_str;

NN_str nn;
int n_data;
T_str data[MAX_TRAIN];
int w_limit = 10;

void read_training_data(f_name)
	char	*f_name;
{
	int i;
	int k;
	int s = 0;
	char ch;
	FILE *fp;
	
	printf("Read training data...\n");

	if ((fp=fopen(f_name,"r"))==NULL){
		fprintf(stderr, "ERROR:Training data file unknown\n");
		exit(1);
	}

	fscanf(fp,"%d %d %d \n", &nn.n_input, &nn.n_hidden, &nn.n_output);
	fscanf(fp,"%d\n", &n_data);
	
	printf("The number of neurons of intput layer is %d\n", nn.n_input);
	printf("The number of neurons of hidden layer is %d\n", nn.n_hidden);
	printf("The number of neurons of output layer is %d\n", nn.n_output);
	printf("The number of learning data is  %d\n\n", n_data);
	
	for(s = 0; s < n_data; s++){
		for (i = 0; i < nn.n_input; i++){
			while ((ch = fgetc(fp)) == '\n');
				data[s].i_value[i] = value(ch);
		}
		fgetc(fp);
	
		for (k = 0; k < nn.n_output; k++){
			while((ch = fgetc(fp)) == '\n');
				data[s].o_value[k] = value(ch);
		}
		fscanf(fp, "\n");
	}
	
	for (s = 0; s < n_data; s++){
		for(i = 0; i < nn.n_input; i++){
			if(i == 0)printf("[%d]input:", s);
			else if (( i % w_limit) == 0) printf("\n    ");
			printf("%1c", CHAR(data[s].i_value[i]));
		}
		printf("\n");
	
		for(k = 0; k < nn.n_output; k++){
			if(k == 0) printf("  output:  ");
			else if (( k% w_limit) == 0 ) printf("\n   ");
			printf("%1c", CHAR(data[s].o_value[k]));
		}	
		printf("\n");
	}
	printf("\n");
} 

void weight_initialize()
{
	int	i;
	int	j;
	int	k;
			
	printf("Initialize weight value...\n\n");

	for(i=0;i<nn.n_input;i++)
		for(j=0;j<nn.n_hidden;j++)
			nn.i_to_h[i][j]=RAND(-0.5,0.5);

	for(j=0;j<nn.n_hidden;j++)
		for(k=0;k<nn.n_output;k++)
			nn.h_to_o[j][k]=RAND(-0.5,0.5);

	for(j=0;j<nn.n_hidden;j++)
		nn.h_offset[j]=RAND(-0.05,0.05);

	for(k=0;k<nn.n_output;k++)
		nn.o_offset[k]=RAND(-0.05,0.05);
				
}

void active_nn()
{
	int	i;
	int	j;
	int	k;
			
	float tmp;
			

	for(j=0;j<nn.n_hidden;j++){
		tmp=nn.h_offset[j];
		for(i=0;i<nn.n_input;i++)
			tmp+=nn.i_to_h[i][j]*nn.i_value[i];
		nn.h_value[j]=f(tmp);
	}

	for(k=0;k<nn.n_output;k++){
		tmp=nn.o_offset[k];
		for(j=0;j<nn.n_hidden;j++)
			tmp+=nn.h_to_o[j][k]*nn.h_value[j];
	nn.o_value[k]=f(tmp);
	}
}

void learning()
{
	int	i;
	int	j;
	int	k;
	int	s=0;
	int	t=0;
	float	tmp;
	float	old;
	float	diff;
	float	h_err[MAX_NEURONS];
	float	o_err[MAX_NEURONS];
	float ih_moment[MAX_NEURONS][MAX_NEURONS];
	float ho_moment[MAX_NEURONS][MAX_NEURONS];
	  
	printf("Learning neural network...\n");
			
	for(i=0;i<nn.n_input;i++)
		for(j=0;j<nn.n_hidden;j++)
			ih_moment[i][j]=0;

	for(j=0;j<nn.n_hidden;j++)
		for(k=0;k<nn.n_output;k++)
			ho_moment[j][k]=0;
											
	do{
		diff=0.0;
		for(s=0;s<n_data;s++){
			nn.i_value=data[s].i_value;
			active_nn();

			for(k=0;k<nn.n_output;k++){
				o_err[k]=(data[s].o_value[k]-nn.o_value[k])*
				nn.o_value[k]*(1-nn.o_value[k]);
				diff+=abs((data[s].o_value[k]-nn.o_value[k]));
			}

			for(j=0;j<nn.n_hidden;j++){
				h_err[j]=0;
				for(k=0;k<nn.n_output;k++)
				h_err[j]+=o_err[k]*nn.h_to_o[j][k]*nn.h_value[j]*(1-nn.h_value[j]);
			}

			for(j=0;j<nn.n_hidden;j++)
				for(k=0;k<nn.n_output;k++){
					old=nn.h_to_o[j][k];
					nn.h_to_o[j][k]+=ETHA*o_err[k]*nn.h_value[j]+ALPHA*ho_moment[j][k];						
					ho_moment[j][k]=nn.h_to_o[j][k]-old;
					nn.o_offset[k]+=BETHA*o_err[k];
				}
				
			for(i=0;i<nn.n_input;i++)
				for(j=0;j<nn.n_hidden;j++){
					old=nn.i_to_h[i][j];
					nn.i_to_h[i][j]+=ETHA*h_err[j]*data[s].i_value[i]+ALPHA*ih_moment[i][j];
					ih_moment[i][j]=nn.i_to_h[i][j]-old;
					nn.h_offset[j]+=BETHA*h_err[j];
				}
		}
		if((t%100)==0)
			printf("[%d]diff is %f\n",t,diff);
		t++;

	}while((diff>DIFF_MIN)&&(t<MAX_LOOP));

	if((t%100)!=0)
		printf("[%d]diff is %f\n\n", t,diff);
	else	printf("\n");

}

void learn_weight(f_name)
	char *f_name;
{
	read_training_data(f_name);
	weight_initialize();
	learning();

}

void read_weight(f_name)
	char *f_name;
{
	int i;
	int j;
	int k;
	FILE *fp;

	if((fp=fopen(f_name,"r"))==NULL){
        printf("Error:The trained weight file open error!!!\n");
		exit(1);
	}
	printf("Read trained weight file...\n");

	fscanf(fp,"%d %d %d\n",&nn.n_input,&nn.n_hidden,&nn.n_output);
	printf("The number of neurons of input layer is %d\n",nn.n_input);
	printf("The number of neurons of hidden layer is %d\n",nn.n_hidden);
	printf("The number of neurons of output layer is %d\n\n",nn.n_output);

	for(i=0;i<nn.n_input;i++)
		for(j=0;j<nn.n_hidden;j++)
			fscanf(fp,"%f\n",&nn.i_to_h[i][j]);

	for(j=0;j<nn.n_hidden;j++)
		for(k=0;k<nn.n_output;k++)
			fscanf(fp,"%f\n",&nn.h_to_o[j][k]);

	for(j=0;j<nn.n_hidden;j++)
			fscanf(fp,"%f\n",&nn.h_offset[j]);

	for(k=0;k<nn.n_output;k++)
			fscanf(fp,"%f\n",&nn.o_offset[k]);

	fclose(fp);

}

void write_weight(f_name)
	char *f_name;
{
	int i;
	int j;
	int k;
	FILE *fp;

	if((fp=fopen(f_name,"w"))==NULL){
        printf("ERROR:File creation error!!!\n");
		exit(1);
	}
	printf("Write trained weight and offset...\n");

	fprintf(fp,"%d %d %d\n",nn.n_input,nn.n_hidden,nn.n_output);

	for(i=0;i<nn.n_input;i++)
		for(j=0;j<nn.n_hidden;j++)
			fprintf(fp,"%f\n",nn.i_to_h[i][j]);

	for(j=0;j<nn.n_hidden;j++)
		for(k=0;k<nn.n_output;k++)
			fprintf(fp,"%f\n",nn.h_to_o[j][k]);

	for(j=0;j<nn.n_hidden;j++)
			fprintf(fp,"%f\n",nn.h_offset[j]);

	for(k=0;k<nn.n_output;k++)
			fprintf(fp,"%f\n",nn.o_offset[k]);

	fclose(fp);

}

void read_test_data(f_name)
	char *f_name;
{
	int i;
	int s=0;
	char ch;
	FILE *fp;

	if((fp=fopen(f_name,"r"))==NULL){
        fprintf(stderr,"ERROR:Testing file unknown\n");
		exit(1);
	}
	printf("Read test data...\n");

	fscanf(fp,"%d\n",&n_data);
	printf("The number of test data is %d\n\n",n_data);

	for(s=0;s<n_data;s++){
		for(i=0;i<nn.n_input;i++){
			while((ch=fgetc(fp))=='\n');
			data[s].i_value[i]=value(ch);	 
		}
		fgetc(fp);
	}
}

void testing()
{
	int i;
	int j;
	int k;
	int s=0;
	float tmp;
	printf("Testing...\n\n");

	for (s=0; s<n_data; s++)
	{
		nn.i_value = data[s].i_value;
		active_nn();

		for(i=0; i<nn.n_input; i++){
			if(i==0) printf(" [%d]input:",s);
			else if((i%w_limit) ==0 ) printf("\n ");
			printf("%1c",CHAR(data[s].i_value[i]));
		}
		printf("\n");
		
		for(k=0; k<nn.n_output; k++){
			if(k == 0) printf(" output: ");
			else if((k%w_limit) == 0) printf("\n ");
			printf(" %5.4f ",nn.o_value[k]);
		}
		printf("\n");
	}
}

void test(f_name)
	char *f_name;
{
	read_test_data(f_name);
	testing();
}

void usage()
{
	printf("usage: bp[-t <train_file> | -r<learned_weight_file> -- for set weight\n");
	printf(" {-s<new_learned_weight_file>} -- for save weight\n");
	printf(" {-e<test_data_file>} -- for test\n");
	printf(" {-w <num>} -- for echo print\n");
	exit(1);
}

void none_fun()
{
}  

void (*set_weight)();
void (*show_weight)();
void (*test_data)();

#define def_set_weight(x)  set_weight = x;
#define def_show_weight(x) show_weight = x;
#define def_test_data(x)   test_data=x;

int main(argc,argv)
	int argc; 
	char *argv[];
{
	int i;
	char *if_name;
	char *of_name;
	char *tf_name;

	if(argc < 3) usage();

	def_show_weight(none_fun);
	def_test_data(none_fun);
	i = 1;
	
	
	while(i<argc){
		if(argv[i][0]=='-')
			switch(argv[i][1]){

			case 't':
				def_set_weight(learn_weight);
				i++;
				if_name = argv[i];
				break;

			case 'r':
				def_set_weight(read_weight);
				i++;
				if_name = argv[i];
				break;

			case 's':
				def_show_weight(write_weight);
				i++;
				of_name = argv[i];
				break;

			case 'e':
				def_test_data(test);
				i++;
				tf_name = argv[i];
				break;

			case 'w':
				i++;
				w_limit = atoi(argv[i]);
				break;

			default: usage();
		}else
			usage();
		i++;
	}
	srand(5);
	set_weight(if_name);
	show_weight(of_name);
	test_data(tf_name);
}
