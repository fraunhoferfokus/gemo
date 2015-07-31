#!/bin/bash -x
# cd path-to-repo-folder/etc/storage_init
# bash init_storage.sh <host><:port> <user:password>

## Delete all tables ###
#echo "Deleting all tables for MDC Storage ..."
#RET=`curl --user $2 -k -s $1/storage/search?query=DROP%20SCHEMA%20public%20CASCADE;`

### Initialize all tables ###
echo -e "\nInitializing the MDC Storage tables ..."
echo
#RET=`curl --user $2 -k -s $1/storage/search?query=CREATE%20SCHEMA%20public;`
RET=`curl --user $2 -k -s -F tableName=users -F file=@users.csv $1/storage/upload`
SUCCEEDED=true
echo "users: $RET"
if [ "$RET" != "Table users created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=e_vehicles -F file=@eVehicles.xml $1/storage/upload`
echo "e_vehicles: $RET"
if [ "$RET" != "Table e_vehicles created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=charging_stations -F file=@chargingStations.csv $1/storage/upload`
echo "charging_stations: $RET"
if [ "$RET" != "Table charging_stations created..." ]; then SUCCEEDED=false; fi
echo 
RET=`curl --user $2 -k -s -F tableName=charging_station_reservation -F file=@chargingStationReservation.xml $1/storage/upload`
echo "charging_station_reservation: $RET"
if [ "$RET" != "Table charging_station_reservation created..." ]; then SUCCEEDED=false; fi
echo 
RET=`curl --user $2 -k -s -F tableName=e_vehicle_positions -F file=@eVehiclePositions.csv $1/storage/upload`
echo "e_vehicle_positions: $RET"
if [ "$RET" != "Table e_vehicle_positions created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=e_vehicle_reservation -F file=@eVehicleReservation.xml $1/storage/upload`
echo "efzreservation: $RET"
if [ "$RET" != "Table e_vehicle_reservation created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=e_vehicle_damage -F file=@e_vehicle_damage.csv $1/storage/upload`
echo "e_vehicle_damage: $RET"
if [ "$RET" != "Table e_vehicle_damage created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=e_vehicle_reservation_details -F file=@eVehicleReservationDetails.xml $1/storage/upload`
echo "e_vehicle_reservation_details: $RET"
if [ "$RET" != "Table e_vehicle_reservation_details created..." ]; then SUCCEEDED=false; fi
echo
RET=`curl --user $2 -k -s -F tableName=payments -F file=@payments.xml $1/storage/upload`
echo "payments: $RET"
if [ "$RET" != "Table payments created..." ]; then SUCCEEDED=false; fi
echo
#Add Complaints	
#Add Complaints	for Ecar_00001
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description= rechter rueckspiegel haengt lose " -d "tags=spiegel,rechts" -d "eVehicleID=ecar_00001" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00001 complaint: "
if echo $RET | grep -Fq Created; then echo "201 created..."; fi
#if [[ !$RET =~ "201 Created" ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description=linker kotfluegel hat schrammen " -d "tags=schrammen,kotfluegel" -d "eVehicleID=ecar_00001" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00001 complaint: "
if echo $RET | grep -Fq Created; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
#Add Complaints	for Ecar_00002
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der linken hinteren Tuer  " -d "tags=tuer,rechts" -d "eVehicleID=ecar_00002" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00002 complaint: "
if echo $RET | grep -Fq Created; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description= scheinwerfer vorne rechts defekt " -d "tags=scheinwerfer,rechts" -d "eVehicleID=ecar_00002" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00002 complaint: "
if echo $RET | grep -Fq Created; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
#Add Complaints	for Ecar_00003
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description= linker rueckspiegel haengt lose " -d "tags=spiegel,links" -d "eVehicleID=ecar_00003" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00003 complaint: "
if echo $RET | grep -Fq Created; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl --user $2 -k -i -s -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der rechten Beifahrertuer" -d "tags=schramme,tuer" -d "eVehicleID=ecar_00003" -d "latitude=52" -d "longitude=13" $1/partizipation/complaints`
echo -n "Ecar_00003 complaint: "
if echo $RET | grep -Fq Created; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
if $SUCCEEDED; then echo -e '...SUCCEEDED!\n'; else echo -e '...FAILED!\n'; exit 1; fi #
