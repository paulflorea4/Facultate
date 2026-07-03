#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <unistd.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdlib.h>

int main(){
        int s;
        struct sockaddr_in server,client;
        int c,l;

        s=socket(AF_INET,SOCK_STREAM,0);
        if(s<0){
                printf("Eroare la crearea socketului server\n");
                return 1;
        }

        memset(&server,0,sizeof(server));
        server.sin_port=htons(1234);
        server.sin_family=AF_INET;
        server.sin_addr.s_addr=INADDR_ANY;

        if(bind(s,(struct sockaddr* ) &server,sizeof(server))<0){
                printf("Eroare la bind\n");
                return 1;
        }

	listen(s,5);

        l=sizeof(client);
        memset(&client,0,sizeof(client));

        while(1){
                c=accept(s,(struct sockaddr *) &client, &l);
                printf("S-a conectat un client\n");
		pid_t pid=fork();
		if(pid==0){
			uint16_t sir_len,len,i;
			char sir[100];
                	if(recv(c,&sir_len,sizeof(sir_len),MSG_WAITALL)<=0){
				close(c);
				exit(0);
			}
			sir_len=ntohs(sir_len);

			int n=recv(c,sir,sir_len,MSG_WAITALL);

                	if(n<=0){
                        	close(c);
                        	exit(0);
                	}
			sir[n]='\0';

                	if(recv(c,&len,sizeof(len),MSG_WAITALL)<=0 
					|| recv(c,&i,sizeof(i),MSG_WAITALL)<=0){
                        	close(c);
				exit(0);
                	}

                	i=ntohs(i);
                	len=ntohs(len);

                	if(i>=n)
                        	len=0;
                	else if(i+len>n)
                        	len=n-i;

                	send(c,sir+i,len,0);
                	close(c);
			close(s);
			exit(0);
		}
		else if(pid>0){
			close(c);
		}
		else{
			printf("Error fork\n");
			close(c);
		}

        }
	close(s);
        return 0;
}
