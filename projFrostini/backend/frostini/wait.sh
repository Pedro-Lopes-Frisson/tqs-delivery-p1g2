#!/bin/bash


while ! curl rabbitmq:15672; do
  sleep 7
done

curl postgresdbfrostini:5432
RESP_STATUS=$?
RESP_52=52

while [[ $RESP_STATUS -eq 7 ]]; do
  sleep 1
  curl postgresdbfrostini:5432
  RESP_STATUS=$?
  echo $RESP_STATUS
done

java -cp app:app/lib/* ua.tqs.frostini.FrostiniApplication
