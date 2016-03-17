package r12D11.pokemon;

import robocode.*;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Porygon2 - a robot by Alejandro Jiménez Pereira(2balric@gmail.com), David Abad Vich(davidabad10@gmail.com)
 */

public class Porygon2 extends AdvancedRobot
{
	
	double previousEnergy = 100; //Última energia guardada cuando solo queda 1 enemigo
  	int movementDirection = 1;
  	int gunDirection = 1;
	Map<String,objetivo> enemigos; //Mapa donde guardamos la información de los enemigos
	int direction=1;
	String key="_"; //Nombre del objetivo
	int midpointcount = 0;
	double midpointstrength = 0;
	//double xdist=9999999; //Distancia del objetivo
	
	/**
	* getRange: Devuelve la distancia de un punto a otro
	*/
	public double getRange( double x1,double y1, double x2,double y2 ){
		double xo = x2-x1;
		double yo = y2-y1;
		double h = Math.sqrt( xo*xo + yo*yo );
		return h;	
	}
	
	/**
	*	antiGravMove: Algoritmo con el que se mueve el tanque
	*/
	void antiGravMove() {
   		double xforce = 0;
	    double yforce = 0;
	    double force;
	    double ang;
	    GravPoint p;
		objetivo en;
    	Collection<objetivo> c = enemigos.values();   
	    Iterator<objetivo> itr = c.iterator();
	
		while (itr.hasNext()){
			en=(objetivo)itr.next();
			
			p = new GravPoint(en.posicion[0].x,en.posicion[0].y, -1000);
		    force = p.power/Math.pow(getRange(getX(),getY(),p.x,p.y),2);
		    //Encontrar la direccion desde el punto hasta nosotros
		    ang = NormaliseBearing(Math.PI/2 - Math.atan2(getY() - p.y, getX() - p.x)); 
		    //Añadir los componentes de esta fuerza a la fuerza total en sus respectivas direcciones
		    xforce += Math.sin(ang) * force;
		    yforce += Math.cos(ang) * force;
			
	    }
	    
		/**La siguiente sección añade un punto medio con un aleatorio (positivo o negativo) de fuerza.
		La fuerza cambia cada 5 vueltas, y va entre -1000 y 1000.*/
		midpointcount++;
		if (midpointcount > 5) {
			midpointcount = 0;
			midpointstrength = (Math.random() * 2000) - 1000;
		}
	
		p = new GravPoint(getBattleFieldWidth()/2, getBattleFieldHeight()/2, midpointstrength);
		force = p.power/Math.pow(getRange(getX(),getY(),p.x,p.y),1.5);
	    ang = NormaliseBearing(Math.PI/2 - Math.atan2(getY() - p.y, getX() - p.x)); 
	    xforce += Math.sin(ang) * force;
	    yforce += Math.cos(ang) * force;
	   
	    /**Las siguientes cuatro líneas hacen que el tanque evada la pared. Sólo nos va a afectar si el bot está cerca
		de las paredes.**/
	    xforce += 5000/Math.pow(getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
	    xforce -= 5000/Math.pow(getRange(getX(), getY(), 0, getY()), 3);
	    yforce += 5000/Math.pow(getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
	    yforce -= 5000/Math.pow(getRange(getX(), getY(), getX(), 0), 3);
	    
	    //Move in the direction of our resolved force.
	    goTo(getX()-xforce,getY()-yforce);
	}
	
	/**
	*	goTo: Avanzar hacia una coordenadas X e Y.
	*/
	void goTo(double x, double y) {
	    double dist = 20; 
	    double angle = Math.toDegrees(absbearing(getX(),getY(),x,y));
	    double r = turnTo(angle);
	    setAhead(dist * r);
	}
	
	/**
	*	NormaliseBearing: Normaliza la posicion relativa
	*/
	double NormaliseBearing(double ang) {
		if (ang > Math.PI)
			ang -= 2*Math.PI;
		if (ang < -Math.PI)
			ang += 2*Math.PI;
		return ang;
	}
		
	/**
	*	escanear: Si hay más de un enemigo escanea continuamente todo el campo, si no fija el radar en el objetivo
	*/
	void escanear() {
		
		Iterator<String> iterator = enemigos.keySet().iterator();
		key = (String) iterator.next();
		
		double radarOffset;
		
		//Si hace tiempo que no localizamos a nadie
		if (getTime() - enemigos.get(key).ctime > 6) {
			radarOffset = 360;	//lo rotamos 360º
		} else {
			//Calculamos el giro necesario del radar para seguir al objetivo
			radarOffset = getRadarHeadingRadians() -
			absbearing(getX(),getY(),enemigos.get(key).posicion[0].x,enemigos.get(key).posicion[0].y);
			//Calculamos el offset debido al seguimiento del objetivo
			//para no perderlo
			if (radarOffset < 0)
				radarOffset -= Math.PI/10;
			else
				radarOffset += Math.PI/10;
		}
		
		//giramos el radar
		setTurnRadarLeftRadians(NormaliseBearing(radarOffset));
	}
	
	/**
	*	turnTo: Gira el tanque "angle" grados de forma que conlleve menos tiempo posible
	*/
	int turnTo(double angle) {
	    double ang;
    	int dir;
	    ang = NormaliseBearing(getHeading() - angle);
	    if (ang > 90) {
	        ang -= 180;
	        dir = -1;
	    }
	    else if (ang < -90) {
	        ang += 180;
	        dir = -1;
	    }
	    else {
	        dir = 1;
	    }
	    setTurnLeft(ang);
	    return dir;
	}

	/**
	*	absbearing: Devuelve los grados absolutos
	*/
	public double absbearing( double x1,double y1, double x2,double y2 ){
		double xo = x2-x1;
		double yo = y2-y1;
		double h = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );;
		if( xo > 0 && yo > 0 ){
			return Math.asin( xo / h );
		}if( xo > 0 && yo < 0 ){
			return Math.PI - Math.asin( xo / h );
		}if( xo < 0 && yo < 0 ){
			return Math.PI + Math.asin( -xo / h );
		}if( xo < 0 && yo > 0 ){
			return 2.0*Math.PI - Math.asin( -xo / h );
		}
		return 0;
	}
	
	/**
	*	disparar: Gira el arma anticipando la posicion del enemigo y dispara a esa posicion con una potencia que depende de la distancia
	*/
	void disparar(){
		if(getOthers()>0){
			double x=enemigos.get(key).guessX(getTime());
			double y=enemigos.get(key).guessY(getTime());
			double gunOffset = getGunHeadingRadians()-absbearing(getX(),getY(),x,y);		

			setTurnGunLeftRadians(NormaliseBearing(gunOffset));	
			
			if (getEnergy()>1){
				if (enemigos.get(key).distancia <50){
					setFire(Rules.MAX_BULLET_POWER);
					scan();
				}
				if (enemigos.get(key).distancia <200){
					setFire(2.8);
				}else if (enemigos.get(key).distancia <350){
					setFire(1.8);
				}else if (enemigos.get(key).distancia <550){
					setFire(1.2);
				}else{
					setFire(1);
				}
			}	
		}			
	}
	
	/**
	 * run: Porygon2's default behavior
	 */
	public void run() {
		
		enemigos = new HashMap<String,objetivo>();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		setColors(new Color(0,255,255),new Color(200,80,80),new Color(210,40,210)); // body,gun,radar
		setBulletColor(new Color(197,75,140));
		
		turnRadarRight(360);
		
		while(true) {
			if(getOthers()==1){
				setBodyColor(new Color(200,80,80));
				setRadarColor(new Color(0,255,255));
				setGunColor(new Color(210,40,210));
				escanear();
				disparar();
				execute();
			}else{
				setTurnRadarRight(90);
				disparar();
				antiGravMove();
				execute();
			}
		}
	}

	/**
	 * onScannedRobot: Cuando escaneas un robot actualizo sus datos en el mapa
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if(key=="_"){
			key=e.getName();
		}
		double absbearing_rad=(getHeadingRadians()+e.getBearingRadians())%(2*Math.PI);
		double _x=getX()+Math.sin(absbearing_rad)*e.getDistance();
		double _y=getY()+Math.cos(absbearing_rad)*e.getDistance();
		double _veloz=e.getVelocity();

		objetivo cnt = (objetivo) (enemigos.get(e.getName()));
		if (cnt == null) { //Si es la primera vez que lo escaneo lo inserto en el mapa
			objetivo aux=new objetivo(e.getName(), e.getEnergy(), e.getDistance(), _x,_y,_veloz,e.getHeading(),e.getTime());
			enemigos.put(e.getName(),aux);
		} else { //Si ya está en el mapa actualizo sus datos
			enemigos.get(e.getName()).add(e.getEnergy(), e.getDistance(), _x, _y, _veloz, e.getHeading(),getTime());			
		}
		
		//En muchos contra muchos cambio de objetivo dependiendo de cual esté más cerca
		if(e.getDistance() < enemigos.get(key).distancia){
			//xdist=e.getDistance();
			key=e.getName();
		}
	
		System.out.println("#enemigos: "+enemigos.size());
		
		//ESQUIVAR-begin
		if(getOthers()==1){
			setTurnRight(e.getBearing()+90-30*movementDirection);
         
		    // Si el robot tiene una perdida de energia asume que este ha disparado
		    double changeInEnergy = previousEnergy-e.getEnergy();
		    if (changeInEnergy>0 && changeInEnergy<=3) {
         	// Esquiva
				movementDirection = -movementDirection;
				if(getRange(getX(), getY(), getBattleFieldWidth(), getY()) < (e.getDistance()/4+25) ){
					setAhead(getRange(getX(), getY(), getBattleFieldWidth(), getY()));
				}else{
					if(getRange(getX(), getY(), 0, getY()) < (e.getDistance()/4+25) ){
						setAhead(getRange(getX(), getY(), 0, getY()));
					}else{
						if( getRange(getX(), getY(), getX(), getBattleFieldHeight()) < (e.getDistance()/4+25) ){
							setAhead(getRange(getX(), getY(), getX(), getBattleFieldHeight()));
						}else{
							if( getRange(getX(), getY(), getX(), 0) < (e.getDistance()/4+25) ){
								setAhead(getRange(getX(), getY(), getX(), 0));
							}else{
								setAhead((e.getDistance()/4+25)*movementDirection);
							}
						}
					}
				}

		     }

		    previousEnergy = e.getEnergy();
		}
		//ESQUIVAR-end
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		if (e.getBearing()<90 && e.getBearing()>-90) {
			setAhead(200);
		} else {
			setBack(200);
		}
	}
	
	public void onHitRobot(HitRobotEvent e) {
		// Si esta delante nuestra, echamos para atrás
		if (e.getBearing() > -90 && e.getBearing() < 90)
			setBack(100);
		// Si está detrás nos desplazamos en sentido contrario
		else
			setAhead(100);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Si esta delante nuestra, echamos para atrás
		if (e.getBearing() > -90 && e.getBearing() < 90)
			setBack(100);
		// Si está detrás nos desplazamos en sentido contrario
		else
			setAhead(100);
	}	

	public void onRobotDeath(RobotDeathEvent e) {
		enemigos.remove(e.getName());
		if(getOthers()>0){
			Iterator<String> iterator = enemigos.keySet().iterator();
			key = (String) iterator.next();
		}
	}
	
	public void onWin(WinEvent event) {    
         System.out.println("¡Hasta la vista baby!");
    } 
}
