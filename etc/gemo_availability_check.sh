RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/allgemeineEfzDaten/application.wadl`
SUCCEEDED=true
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/allgemeineLadestationenDaten/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/verfuegbarkeitEfz/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/verfuegbarkeitLadestation/application.wadl`
 if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/positionEfz/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/positionLadestation/application.wadl`
 if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/fixmycity/cm/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/participation/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/opnv/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
#RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/odrClientProxy/application.wadl` if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
#echo		TODO!
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/openid/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/payments/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/storage/application.wadl`
 if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/reservierungEfz/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/reservierungLadestation/application.wadl`
 if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
RET=`curl -o /dev/null --silent --write-out '%{http_code}\n' --user $2 -k -s $1/userprofile/application.wadl` 
if [[ ! $RET =~ .*200.* ]]; then echo $RET; SUCCEEDED=false; else echo "GeMo Service available..."; fi 
echo
if $SUCCEEDED; then echo -e '...SUCCEEDED!\n'; else echo -e '...FAILED!\n'; exit 1; fi #