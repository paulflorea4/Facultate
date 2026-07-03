#!/bin/bash

while true; do
    read -p "Da-mi doua numere: " X Y
    if echo $X | grep -E -q "^[0-9]+$" &&
       echo $Y | grep -E -q "^[0-9]+$" &&
       test $X -gt $Y; then
        break
    fi
done
