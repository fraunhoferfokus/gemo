#Initialize complaints tables
echo
RET=`curl $3 --user $2 -k -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -F tableName=e_vehicle_damage -F file=@e_vehicle_damage.csv  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table e_vehicle_damage created..."; fi
echo
RET=`curl $3 --user $2 -k -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -F tableName=charger_damage -F file=@charger_damage.csv  -g $1/storage/upload`
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table charger_damage created..."; fi
echo

# Add Complaints	
# Add Complaints	for ecar_00001
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= rechter rueckspiegel haengt lose " -d "tags=spiegel,rechts" -d "eVehicleID=ecar_00001" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "ecar_00001 complaint: "
if [[ ! $RET =~ .*ecar_00001.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description=linker kotfluegel hat schrammen " -d "tags=schrammen,kotfluegel" -d "eVehicleID=ecar_00001" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "Ecar_00001 complaint: "
if [[ ! $RET =~ .*ecar_00001.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
# Add Complaints	for Ecar_00002
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der linken hinteren Tuer  " -d "tags=tuer,rechts" -d "eVehicleID=ecar_00002" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "Ecar_00002 complaint: "
if [[ ! $RET =~ .*ecar_00002.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= scheinwerfer vorne rechts defekt " -d "tags=scheinwerfer,rechts" -d "eVehicleID=ecar_00002" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "Ecar_00002 complaint: "
if [[ ! $RET =~ .*ecar_00002.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
# Add Complaints	for Ecar_00003
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= linker rueckspiegel haengt lose " -d "tags=spiegel,links" -d "eVehicleID=ecar_00003" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "Ecar_00003 complaint: "
if [[ ! $RET =~ .*ecar_00003.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der rechten Beifahrertuer" -d "tags=schramme,tuer" -d "eVehicleID=ecar_00003" -d "latitude=52" -d "longitude=13" -d "type=e_vehicle" -g $1/participation/complaints`
echo -n "Ecar_00003 complaint: "
if [[ ! $RET =~ .*ecar_00003.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi

# Add Complaints	for ChargingStation_00001
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= rechter rueckspiegel haengt lose " -d "tags=spiegel,rechts" -d "eVehicleID=charger_00001" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00001 complaint: "
if [[ ! $RET =~ .*charger_00001.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description=linker kotfluegel hat schrammen " -d "tags=schrammen,kotfluegel" -d "eVehicleID=charger_00001" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00001 complaint: "
if [[ ! $RET =~ .*charger_00001.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
# Add Complaints	for charger_00002
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der linken hinteren Tuer  " -d "tags=tuer,rechts" -d "eVehicleID=charger_00002" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00002 complaint: "
if [[ ! $RET =~ .*charger_00002.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= scheinwerfer vorne rechts defekt " -d "tags=scheinwerfer,rechts" -d "eVehicleID=charger_00002" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00002 complaint: "
if [[ ! $RET =~ .*charger_00002.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
# Add Complaints	for charger_00003
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= linker rueckspiegel haengt lose " -d "tags=spiegel,links" -d "eVehicleID=charger_00003" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00003 complaint: "
if [[ ! $RET =~ .*charger_00003.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
RET=`curl $3 --user $2 -k -i -s -H "accessToken:39393939kkk000" -H "scope:write" -H "username:developer" -X POST -d "title=Damage" -d "description= tiefe horizontale Schramme an der rechten Beifahrertuer" -d "tags=schramme,tuer" -d "eVehicleID=charger_00003" -d "latitude=52" -d "longitude=13" -d "type=charging_station" -g $1/participation/complaints`
echo -n "charger_00003 complaint: "
if [[ ! $RET =~ .*charger_00003.* ]]; then echo $RET; SUCCEEDED=false; else echo "201 created..."; fi
echo
 if $SUCCEEDED; then echo -e '...SUCCEEDED!\n'; else echo -e '...FAILED!\n'; exit 1; fi #