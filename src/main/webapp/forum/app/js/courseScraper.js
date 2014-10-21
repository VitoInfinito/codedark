function scrape(){
	function Course() {
		courseCode: ;
		courseName: ;
	}

	var fetchedCourses = [];

	$.ajax({
		url: 'https://www.student.chalmers.se/sp/course_list?flag=1&query_start=1&batch_size=1310&sortorder=CODE&search_ac_year=2014/2015',
		type: 'get',
		dataType: 'html',
		success: function(data) {
			data = data.replace(/<img\b[^>]*>/ig, '');
			data = data.replace(/<script.*?>([\s\S]*?)<\/script>/gmi, '');
			var tBody = $(data).find('table')[2].tBodies[0];
			var secondTbl = tBody.rows[0].cells[0];
			console.log(secondTbl+ "Hej");
			console.log($(data).find('table').children('tbody')[12]);		
		}
	});

}


scrape();





