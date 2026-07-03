#!/bin/bash

while test 3 -lt `who | wc -l `; do
    echo prea multi
    sleep 1
done
