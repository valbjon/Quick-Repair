package com.ipipe724.controller;

import java.util.Random;

public class CalculateDistance {
	
		private static double degreesToRadians(double degree) {
			return (double)degree * Math.PI / 180;
		}

		public int distanceInKmBetweenEarthCoordinates(double lat1, double lon1) {
		  int  earthRadiusKm = 6371;
		  
		  double rangeMin = 20;
		  double rangeMax = 40;
		  
		  Random r = new Random();
		  double lat2 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		  double lon2 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		  
		  
		  double  dLat = CalculateDistance.degreesToRadians(lat2-lat1);
		  double  dLon = CalculateDistance.degreesToRadians(lon2-lon1);

		  lat1 = CalculateDistance.degreesToRadians(lat1);
		  lat2 = CalculateDistance.degreesToRadians(lat2);
		  double a =Math.sin(dLat/2) * Math.sin(dLat/2) +
		          Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		  return (int)(6371 * c);
		}
}
