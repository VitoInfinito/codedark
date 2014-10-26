function scrape(){
        if(debugMode) console.log("Initiated scraping");
	function Course() {
		courseCode: ;
		courseName: ;
	}

	var fetchedCourses = [];

	$.ajax({
		url: 'https://www.student.chalmers.se/sp/course_list?flag=1&query_start=1&batch_size=9999&sortorder=CODE&search_ac_year=2014/2015',
		type: 'get',
		dataType: 'html',
		success: function(data) {
			data = data.replace(/<img\b[^>]*>/ig, '');
			data = data.replace(/<script.*?>([\s\S]*?)<\/script>/gmi, '');
			var trElems = $(data).find('table').children('tbody')[12].rows;

			extractCoursesFromHTML(trElems);		
		}
	});

	function extractCoursesFromHTML(trElems){
		$.each(trElems, function(index, val) {
			
			if(index < 2){
				return;
			}else{
				var tmp = new Course();
			 	test = val.innerText.split(/\n/);
			 	tmp.courseCode = $.trim(test[1]);
			 	tmp.courseName = $.trim(test[2]);
			 	fetchedCourses.push(tmp);

			}
		});
	}
    if(debugMode) console.log(fetchedCourses);
    return fetchedCourses;
        
}

var items = scrape();

function retScrapedItems(){
    return items;
}




