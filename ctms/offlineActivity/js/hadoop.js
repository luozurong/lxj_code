function getTime() {
	var currentDate = new Date();
	var yyyy = currentDate.getFullYear().toString();
	var MM = (currentDate.getMonth() + 1).toString();
	if(MM.length == 1)
		MM = "0" + MM;
	var DD = currentDate.getDate().toString();
	if(DD.length == 1)
		DD = "0" + DD;
	var HH = currentDate.getHours().toString();
	if(HH.length == 1)
		HH = "0" + HH;
	var mm = currentDate.getMinutes().toString();
	if(mm.length == 1)
		mm = "0" + mm;
	var ss = currentDate.getSeconds().toString();
	if(ss.length == 1)
		ss = "0" + ss;
	var time_stamp = yyyy + "-" + MM + "-" + DD + " " + HH + ":" + mm + ":" + ss;
	return time_stamp;
}