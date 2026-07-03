#!/bin/bash

echo $PATH | sed -E "s/:/\n/g" | while read D; do
    if [ ! -d $D ]; then
        continue
    fi
    stat --printf="%i %n\n" $D/*| grep -E "^[^ ]*$1"
done
