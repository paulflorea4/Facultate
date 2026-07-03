#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <unistd.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h> 
int main() {
  int c;
  struct sockaddr_in server;
  uint16_t a, b, suma;
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(8889);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("10.51.1.14");
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }

  uint16_t id=(uint16_t) 49466;
  id=htons(id);

  send(c,&id,sizeof(id),0);
  
  uint16_t lungime;

  recv(c,&lungime,sizeof(lungime),0);

  lungime=ntohs(lungime);
  printf("Lungime enuntului: %hu\n",lungime);

  char enunt[500];
  recv(c,enunt,sizeof(enunt[0])*lungime,MSG_WAITALL);
  enunt[lungime]='\0';

  printf(enunt);
  printf("\n");

  lungime=htons(lungime);
  send(c,&lungime,sizeof(lungime),0);

  char length;
  char input[255];
/*
  while(1){

	recv(c,&length,sizeof(length),MSG_WAITALL);

	recv(c,input,sizeof(input[0])*length,MSG_WAITALL);

	input[length]='\0';
  }
*/

  //recv(c,&length,sizeof(length),MSG_WAITALL);
  
  while(1){
	recv(c,&length,sizeof(length),MSG_WAITALL);
	recv(c,input,sizeof(input[0])*length,MSG_WAITALL);
	printf("%s\n",input);
  }
  //printf("%d\n",length);

  close(c);
}
