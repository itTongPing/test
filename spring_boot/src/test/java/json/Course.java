package json;


public class Course {
	      private String courseName;
	      private Integer courseScore;
	      private String courseTeacher;
	      
	      
	      
		public Course() {
		}
		public Course(String courseName, String courseTeacher,
				Integer courseScore) {
			this.courseName = courseName;
			this.courseScore = courseScore;
			this.courseTeacher = courseTeacher;
		}
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public Integer getCourseScore() {
			return courseScore;
		}
		public void setCourseScore(Integer courseScore) {
			this.courseScore = courseScore;
		}
		public String getCourseTeacher() {
			return courseTeacher;
		}
		public void setCourseTeacher(String courseTeacher) {
			this.courseTeacher = courseTeacher;
		}
	      
	      
	      
}
