package r12D11.pokemon;
//import robocode.*;

public class objetivo{
	public String nombre;
	public double energia;
	public double distancia;
	public double head;
	public long ctime;
	public pos[] posicion;
	public double veloz;

	public objetivo(){
		nombre="_";
		distancia=9999999;
		posicion = new pos[3];
		posicion[0]=new pos();
		posicion[1]=new pos();
		posicion[2]=new pos();
		head=0.0;
		ctime=0;
	}
	
	public objetivo(String _nombre, double _energia, double _distancia, double _x, double _y, double _veloz, double _head, long _ctime){
		posicion = new pos[3];
		posicion[0]=new pos();
		posicion[1]=new pos();
		posicion[2]=new pos();
		nombre=_nombre;
		energia=_energia;
		distancia=_distancia;
		posicion[0].x=_x;
		posicion[0].y=_y;
		veloz=_veloz;	
		head=_head;
		ctime=_ctime;
	}

	public void add(double _energia, double _distancia, double _x, double _y, double _veloz, double _head, long _ctime){
		posicion[2].x=posicion[1].x;
		posicion[2].y=posicion[1].y;
		posicion[1].x=posicion[0].x;
		posicion[1].y=posicion[0].y;
		posicion[0].x=_x;
		posicion[0].y=_y;
		energia=_energia;
		distancia=_distancia;
		veloz=_veloz;
		head=_head;
		ctime=_ctime;
	}

	public double guessX(long when){
		long diff = when - ctime;
		return posicion[0].x+Math.sin(head)*veloz*diff;
	}

		
	public double guessY(long when){
		/*long diff = when - ctime;
		return posicion[0].y+Math.cos(head)*veloz*diff;
		*/
		double auno=((posicion[0].x*posicion[0].y)+(posicion[1].x*posicion[1].y)+(posicion[2].x*posicion[2].y))/3;
		double my=(posicion[0].y+posicion[1].y+posicion[2].y)/3;
		double mx=(posicion[0].x+posicion[1].x+posicion[2].x)/3;
		double sxy=auno-mx*my;
		double s2x=(posicion[0].x*posicion[0].x+posicion[1].x*posicion[1].x+posicion[2].x*posicion[2].x)/3;
		double a=sxy/s2x;
		double b=my-a*mx;
		
		return a*guessX(when)+b;
		
	}
}