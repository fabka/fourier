/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fourier;

import java.util.ArrayList;
import java.io.*;
/**
 *
 * @author Fabián Merchán
 * @author Laura Rojas
 * @author Jennifer Harker
 */
public class Fourier {
    
    private final ArrayList<Integer> gt;
    private final int f;
    private int bw;
    private final int n;
    private final double detalle;
    private final double tMax;
            
    public Fourier(int f, ArrayList<Integer> gt, int nMax, double tMax, double detalle){
        this.gt = gt;
        this.f = f;
        this.n = nMax;
        this.detalle = detalle;
        this.tMax = tMax;
    }
        
    public double a0 (){
        int tam = gt.size();
        double limiteSuperior;
        double limiteInferior = 0;
        double a0 = 0;
        for(int i=1; i<=tam; i++){
            limiteSuperior = (double) i/tam;
            if( gt.get(i-1) == 1 )
                a0 += (double) limiteSuperior-limiteInferior;
            limiteInferior = limiteSuperior;
        }
        return a0;
    }
    
    public double an(int n){  
        int tam = gt.size();
        double limiteSuperior;
        double limiteInferior = 0;
        double an = 0;
        for(int i=1; i<=tam; i++){
            limiteSuperior = (double) i/tam;
            if(gt.get(i-1) == 1)
                an += (double) Math.cos(2*Math.PI*n*limiteInferior)-Math.cos(2*Math.PI*n*limiteSuperior);
            limiteInferior = limiteSuperior;
        }
        an /= (double)n*Math.PI*-1;
        return an;
    }
    
    public double bn (int n){
        int tam = gt.size();
        double limiteSuperior;
        double limiteInferior = 0;
        double bn = 0;
        for(int i=1; i<=tam; i++){
            limiteSuperior = (double) i/tam;
            if(gt.get(i-1) == 1)
                bn += (double) Math.sin(2*Math.PI*n*limiteSuperior)-Math.sin(2*Math.PI*n*limiteInferior);
            limiteInferior = limiteSuperior;
        }
        bn /= (double)(n*Math.PI);
        return bn;
    }
    
    public ArrayList<Double> obtenerTiempo(){
        ArrayList<Double> tiempo = new ArrayList<>();
        for(double i=0; i<=tMax; i+=detalle){
            tiempo.add(i);
        }
        return tiempo;
    }
    
    public ArrayList<Double> gt (){
        ArrayList<Double> senal = new ArrayList<>();
        ArrayList<Double> tiempo = obtenerTiempo();
        int tam = tiempo.size();
        double suma;
        
        for( int i=0; i<tam; i++ ){
            suma = 0;
            for( int j=1; j<=this.n; j++ ){
                suma += (double) an(j)*Math.sin(2*Math.PI*j*f*tiempo.get(i)) + bn(j)*Math.cos(2*Math.PI*j*f*tiempo.get(i));
            }
            senal.add((double) suma + a0());
        }
        return senal;
    }
           
    public void exportarExcel( ArrayList<Double> gt ){
        ArrayList<Double> tiempo = obtenerTiempo();
        FileWriter fw = null;
        int tiempoTam = tiempo.size();
        try {
            fw = new FileWriter("Fourier.csv");
            int i = tiempoTam-1;
            for( int j = 0; j<tiempoTam; j++ ){
                fw.write(tiempo.get(j).toString().replace('.', ',') + ";" + gt.get(i).toString().replace('.', ',') + "\n");
                i--;
            }
        } catch (Exception e) {
        } finally {
           try {
           if (null != fw)
              fw.close();
           } catch (Exception e2) {
           }
        }
    }
    
    public void crearSerieFourier (){
        exportarExcel(gt());
    }
}