#!/bin/bash

find a -type f | while read F; do
    if ls -l $F | grep -E -q '^.{8}w'; then
        ls -l $F
        chmod o-w $F
        ls -l $F
        echo "========================================"
    fi
done
