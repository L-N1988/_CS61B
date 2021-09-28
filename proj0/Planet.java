public class Planet {
	public static final double G = 6.67e-11;
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	/** Constructor of Planet class. */
	public Planet(double xP, double yP, double xV, 
			double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	/** Calculates the distance between two Planets. 
	 * Return a double equal to the distance
	 */
	public double calcDistance(Planet p) {
		return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos)
				+ (yyPos - p.yyPos) * (yyPos - p.yyPos));
	}

	/** Calculates the force between two Planets.
	 * Return a double equal to the distance
	 */
	public double calcForceExertedBy(Planet p) {
		return G * mass * p.mass 
			/ (calcDistance(p) * calcDistance(p));
	}

	public double calcForceExertedByX(Planet p) {
		return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
	}

	public double calcForceExertedByY(Planet p) {
		return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
	}

	public double calcNetForceExertedByX(Planet[] p) {
		double netForceX = 0;
		for (Planet s : p) {
			if (s == this) {
				continue;
			} else {
				netForceX += calcForceExertedByX(s);
			}
		}
		return netForceX;
	}

	public double calcNetForceExertedByY(Planet[] p) {
		double netForceY = 0;
		for (Planet s : p) {
			if (s == this) {
				continue;
			} else {
				netForceY += calcForceExertedByY(s);
			}
		}
		return netForceY;
	}

	public void update(double dt, double fX, double fY) {
		double aX, aY;

		aX = fX / mass;
		aY = fY / mass;

		xxVel += aX * dt;
		yyVel += aY * dt;

		xxPos += xxVel * dt;
		yyPos += yyVel * dt;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}
