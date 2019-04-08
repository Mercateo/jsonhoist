function (e) {
	
	e.name = e.fullName;
	e.details = {
			'first':e.firstName,
			'last':e.lastName,
			'age':e.age
	}
}