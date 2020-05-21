package com.parkingapp.homeactivity;

import android.util.Log;

import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public static void AccelerometroTest () throws FileNotFoundException {
        DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");

        ArrayList<String> x= new ArrayList<>();
        ArrayList<String> y= new ArrayList<>();
        ArrayList<String> z= new ArrayList<>();


        File f= new File(".../Prova2.txt");

        double X[];
        double Y[];
        double Z[];


        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

                String arrayString[]=   line.split(",");
                x.add(arrayString[0]);
                y.add(arrayString[1]);
                z.add(arrayString[2]);

            }
            br.close();

        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        X=new double[x.toArray().length];
        Y=new double[y.toArray().length];
        Z=new double[z.toArray().length];

        for (int i=0; i<x.toArray().length; i++)
        {
            X[i]=(double)x.toArray()[i];
            Y[i]=(double)y.toArray()[i];
            Z[i]=(double)z.toArray()[i];
        }

        TimeSeries ts1=new TimeSeries(Y);





        ArrayList<String> x1= new ArrayList<>();
        ArrayList<String> y1= new ArrayList<>();
        ArrayList<String> z1= new ArrayList<>();


        File f1= new File(".../Prova3.txt");

        double X1[];
        double Y1[];
        double Z1[];


        StringBuilder text1 = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

                String arrayString[]=   line.split(",");
                x1.add(arrayString[0]);
                y1.add(arrayString[1]);
                z1.add(arrayString[2]);

            }
            br.close();

        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        X1=new double[x1.toArray().length];
        Y1=new double[y1.toArray().length];
        Z1=new double[z1.toArray().length];

        for (int i=0; i<x.toArray().length; i++)
        {
            X1[i]=(double)x1.toArray()[i];
            Y1[i]=(double)y1.toArray()[i];
            Z1[i]=(double)z1.toArray()[i];
        }




        TimeSeries ts2=new TimeSeries(Y1);

        double d=  com.dtw.FastDTW.getWarpInfoBetween(ts1, ts2, 1, distFn).getDistance();


        Log.i("Distanza: ", " "+ d);
    }
}