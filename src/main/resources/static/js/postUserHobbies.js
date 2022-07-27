// var settings = function(value, storyId){
//     return {
//         "url": "localhost:9090/api/story/"+storyId,
//         "method": "POST",
//         "success" : success,
//         error : function(e) {
//             debugger;
//             alert("Error!")
//             console.log("ERROR: ", e);
//         },
//         "headers": {
//             "Access-Control-Allow-Origin": "*",
//             "Cookie": "JSESSIONID=BB48D3737B906D82A71100815495CE52",
//             "Content-Type": "application/json"
//         },
//         "data": JSON.stringify({
//             "cardValue": value,
//             "user": null
//         }),
//     };
// }
//
// var success = function(response){
//     console.log(response)
// }
//
// $(document).ready(function () {
//     $("#sendHobbies").click(function (){
//         var hobbyNr = 10;
//         var allRated = true;
//         for (let i = 1; i <= hobbyNr; i++) {
//             if ($("input[name="+i+"]:checked").val() == undefined) {
//                 allRated = false;
//             }
//             // else {
//             //     console.log($("input[name="+i+"]:checked").val() + " is not")
//             // }
//         }
//
//         if (!allRated) {
//             alert("Rate every hobby!");
//         }
//         else {
//             for (let hobbyId = 1; hobbyId <= hobbyNr; hobbyId++) {
//                 var userId = $("#regUserId").val();
//                 var hobbyRating= $("input[name="+hobbyId+"]:checked").val();
//
//                 $.ajax({
//                     type : "POST",
//                     contentType : "application/json",
//                     url : "/userhobby/save/user/"+ userId +"/hobby/"+hobbyId+"/rating/"+hobbyRating,
//                     data : JSON.stringify({
//                         "hobby": null,
//                         "user": null,
//                         "rating": hobbyRating,
//
//                     }),
//                     dataType : 'json',
//                     success : function(result) {
//                         console.log("posted");
//                     },
//                 });
//             }
//
//         }
//
//
//
//         console.log(allRated + ", rrated")
//         // console.log($("#regUserId").val() + ", val")
//         // window.location.replace("http://localhost:6060/login");
//
//
//     });
//
// })