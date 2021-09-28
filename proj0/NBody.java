public class NBody {
	private static int N = 0;
	public static double readRadius(String planetsTxtPath) {
		double R;
		In in = new In(planetsTxtPath);
		if (!in.isEmpty()) {
			N = in.readInt();
			R = in.readDouble();
			return R;
		} else {
			return 0.0;
		}
	}

	public static Planet[] readPlanets(String planetsTxtPath) {
		int N = 5;
		double Rmax;
		double xxPos, yyPos, xxVel, yyVel, mass;
		String imgFileName;
		In in = new In(planetsTxtPath);
		if (!in.isEmpty()) {
			N = in.readInt();
			Rmax = in.readDouble();
		}

		Planet[] p = new Planet[N];
		for (int i = 0; i < N; i++) {
			xxPos = in.readDouble();
			yyPos = in.readDouble();
			xxVel = in.readDouble();
			yyVel = in.readDouble();
			mass = in.readDouble();
			imgFileName = in.readString();
			p[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
			if (in.isEmpty()) {
				break;
			}
		}
		return p;
	}

	public static void main(String[] args) {
		double T, dt;
		int i = 0;
		double origin = 0;
		String filename;
		Planet[] planets;
		double R;
		String imgBackground = "images/starfield.jpg";

		T = Double.parseDouble(args[0]);
		dt = Double.parseDouble(args[1]);
		filename = args[2];

		planets = readPlanets(filename);
		R = readRadius(filename);

		/** Create the animation. */
		StdDraw.enableDoubleBuffering();
		double[] xForces = new double[planets.length];
		double[] yForces = new double[planets.length];
		for (origin = 0; origin < T;origin += dt) {
			for (i = 0; i < planets.length; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			for (i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}
			/** Draw the background. */
			StdDraw.setScale(-R, R);
			StdDraw.clear();
			StdDraw.picture(0, 0, imgBackground);
			/** Draw planets on the background. */
			for (Planet s : planets) {
				s.draw();
			}

			StdDraw.show();
			StdDraw.pause(10);
		}

		/** Print the universe. */
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", R);
		for (i = 0; i < planets.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
					planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
					planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
		}
	}
}
