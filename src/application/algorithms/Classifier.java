package application.algorithms;

public class Classifier implements Comparable<Classifier>{
	public Double classes;
	public Double distance;

	public Classifier(double classes, double distance) {
		this.classes = classes;
		this.distance = distance;
	}

	public Double getClasses() {
		return classes;
	}

	public Double getDistance() {
		return distance;
	}

	public int compareTo(Classifier anotherInstance) {

		if (this.distance < anotherInstance.distance) return -1;
        if (this.distance > anotherInstance.distance) return 1;
        return 0;
    }

}
