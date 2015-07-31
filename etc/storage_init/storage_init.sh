#!/bin/bash -x
# cd path-to-repo-folder/etc/storage_init
# bash storage_init.sh <host><:port> <user:password> <iptype>
## Delete all tables ###
#echo "Deleting all tables for MDC Storage ..."
#RET=`curl --user $2 -k -s $1/storage/search?query=DROP%20SCHEMA%20public%20CASCADE;`

### Initialize all tables ###
echo -e "\nInitializing the MDC Storage tables ..."
echo
#RET=`curl --user $2 -k -s $1/storage/search?query=CREATE%20SCHEMA%20public;`
RET=`curl $3 --user $2 -k -s -F tableName=users -F file=@users.csv -g $1/storage/upload`
SUCCEEDED=true
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table users created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=e_vehicles -F file=@eVehicles.xml  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table e_vehicles created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=charging_stations -F file=@chargingStations.csv  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table charging stations created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=charging_station_reservation -F file=@chargingStationReservation.xml  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table charging_station_reservation created..."; fi
echo 
RET=`curl $3 --user $2 -k -s -F tableName=e_vehicle_positions -F file=@eVehiclePositions.csv  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table e_vehicle_positions created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=e_vehicle_reservation -F file=@eVehicleReservation.xml  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table e_vehicle_reservation created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=payments -F file=@payments.xml  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table payments created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=accesstoken -F file=@accesstoken.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table accesstoken created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=refreshtoken -F file=@refreshtoken.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table refreshtoken created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=authcode -F file=@authcode.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table authcode created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=clients -F file=@clients.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table clients created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=scopes -F file=@scopes.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table scopes created..."; fi
echo
RET=`curl $3 --user $2 -k -s -F tableName=services -F file=@services_scopes.csv  -g $1/storage/upload/private`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table services_scopes created..."; fi
echo
if $SUCCEEDED; then echo -e '...SUCCEEDED!\n'; else echo -e '...FAILED!\n'; exit 1; fi #

