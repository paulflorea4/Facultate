#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int hexatoint(char c) {
  if ((c >= 'a') && (c <= 'f'))
    return c - 'a' + 10;
  if ((c >= 'A') && (c <= 'F'))
    return c - 'A' + 10;
  return c - '0';
}

void decode(char *s) {
  int i = 0, j;
  while (s[i] != 0) {
    if (s[i] == '+')
      s[i] = ' ';
    if (s[i] == '%') {
      char c = 16 * hexatoint(s[i+1]) + hexatoint(s[i+2]);
      s[i] = c;
      j = i + 1;
      do {
        s[j] = s[j + 2];
        j++;
      } while (s[j] != 0);
    }
    i++;
  }  
}

int main() {
  printf("Content-type: text/html\n\n");
  printf("<html><body>\n");

  char *query = getenv("QUERY_STRING");

  if (query != NULL && strlen(query) > 0) {
    char *valoare = strchr(query, '=');
    if (valoare != NULL) valoare++;
    else valoare = query;

    char s[1024];
    strncpy(s, valoare, sizeof(s) - 1);
    s[sizeof(s) - 1] = '\0';

    printf("Original string: %s<br>\n", s);
    
    decode(s);
    
    printf("Decoded string: %s", s);
  } else {
    printf("Eroare: Nu s-au primit date.");
  }

  printf("</body></html>\n");
  return 0;
}