function (e) {
	
	e.fullName = e.firstName+' '+e.lastName; 
	if (e.age === null) 
		e.age=999;
	
}