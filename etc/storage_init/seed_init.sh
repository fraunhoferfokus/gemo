echo -e "\nInitializing the hash seeds table ..."
echo
RET=`curl $3 --user $2 -k -s -F tableName=seeds -F file=@seeds.csv -g $1/storage/upload`
SUCCEEDED=true
if [[ ! $RET =~ .*created.* ]]; then echo $RET; SUCCEEDED=false; else echo "Table seeds created..."; fi
echo
 if $SUCCEEDED; then echo -e '...SUCCEEDED!\n'; else echo -e '...FAILED!\n'; exit 1; fi #