/* Nama : Nabilah Erfariani
   NIM  : 13519181
   Kelas: K04
   Tugas Kecil IF2211 Strategi Algoritma
   Cryptarithmetic
*/

import java.io.File; 
import java.util.Scanner; 
import java.util.List;
import java.util.ArrayList;  
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Hashtable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cryptarithmetic {
    /* Deklarasi variabel */
    public int pjgKata;
    public int i;
    public int idx;
    public int[] assignedNumber;
    public int value;
    public int jumlahpercobaan;
    public ArrayList<Integer> listAngkaBoleh = new ArrayList<Integer>();
    public ArrayList<ArrayList<Character>> listhuruf = new ArrayList<ArrayList<Character>>();
    public Hashtable<Character, Integer> jmlAssigned = new Hashtable<Character, Integer>();
    public Hashtable<Character, Integer> solusiAngka = new Hashtable<Character, Integer>();
    
    /* main program */
    public static void main(String[] args){
        /* read file txt to string */
        String nameFile = "../test/input10.txt";
        
        /* read AllLines in file */
        Path getFile = Paths.get(nameFile);
        try{
            /* menampilkan file ke layar */
            File outputFile = new File(nameFile);
            Scanner readerfile = new Scanner(outputFile);
            while (readerfile.hasNextLine()){
                String output = readerfile.nextLine();
                System.out.println(output);
            }
            readerfile.close();

            List<String> kata = Files.readAllLines(getFile, StandardCharsets.UTF_8);

            /* mendapatkan panjang kata hingga sebelum tanda + */
            int pjgKata = kata.size()-1;
            
            /* deklarasi waktu mulai dan waktu selesai perhitungan dengan tipe long in milisecond */
            long waktumulai = System.currentTimeMillis();
            
            /* Proses untuk mencari solusi angka dari setiap huruf */

            Cryptarithmetic cryptarithmetic = new Cryptarithmetic(pjgKata);
            for(int i=0; i<pjgKata-1; i++){
                for(int j = kata.get(i).length()-1; j>=0; j--){
                    char z = kata.get(i).charAt(j);
                    cryptarithmetic.getSolusiAngka(i,z);
                }
            }
            for(int j = kata.get(kata.size()-1).length()-1; j >=0; j--){
                char y = kata.get(pjgKata).charAt(j);
                cryptarithmetic.getSolusiAngka(pjgKata-1,y);
            }
            
            cryptarithmetic.setAngkaBoleh();
            cryptarithmetic.calculate();
            
            long waktuselesai = System.currentTimeMillis();
            long waktueksekusi = (waktuselesai - waktumulai);

            cryptarithmetic.execute();
            System.out.println(" ");
            System.out.println("Waktu eksekusi: " +waktueksekusi+" ms");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /* Dibawah ini Fungsi-fungsi/prosedur-prosedur */

    public boolean isSolved(){
        /* melakukan pengecekan apakah semua huruf sudah terisi angka */
        boolean solved = true;
		for(int i=0;i<listhuruf.size();i++)
		{
			if(listhuruf.get(i).size()!=assignedNumber[i])
				solved=false;
		}
        return solved;
    }

    public void setAngkaBoleh(){
        /* Set angka-angka yang diperbolehkan untuk di-assign ke huruf*/
        for (int i=0;i<=listhuruf.get(pjgKata-1).size();i++){
            listAngkaBoleh.add(0);
        }
    }

    public boolean sudahAda (char huruf){
        /* melakukan pengecekan apakah sudah ada angka yang di-assign pada huruf lainnya*/
        char key=' ';
        boolean udahAda = false;
        Enumeration<Character> enumerasiKey = solusiAngka.keys();
        while(!udahAda && enumerasiKey.hasMoreElements()){
                key = enumerasiKey.nextElement();
                if (solusiAngka.get(key)==value){
                    udahAda = true;}
        }
        if(udahAda){
            if(key != huruf){
                return true;
            }else{
                return false;}
        }
        return udahAda;
    }

    public void getSolusiAngka (int i, char huruf){
        /* assign solusi ke list huruf yg unik */
        listhuruf.get(i).add(huruf);
        jmlAssigned.put(huruf,0);
    }

    public boolean isFirstChar (char huruf){
        /* pengecekan apakah huruf saat ini sama dengan huruf paling depan/huruf pertama */
        boolean firstchar = false;
        for(int i=0; i<pjgKata; i++){
            if(!firstchar){
                int first = listhuruf.get(i).size()-1;
                if(listhuruf.get(i).get(first).equals(huruf)){
                    firstchar = true;
                }
            }
        }
        return firstchar;
    }

    public boolean isInkonsistenValue (char huruf) {
        /* melakukan pengecekan apakah huruf yang sama memiliki nilai yang berbeda */
        if(solusiAngka.getOrDefault(huruf,10) == 10){
            return false;
        } else {
            return value!=solusiAngka.get(huruf);
        }
    }

    public void permutasi(){
        /* melakukan permutasi satu-satu dengan mengecek setiap huruf ke depan */
        value = 0;
        int last = listhuruf.size()-1;
        idx++;
        idx = idx % pjgKata;
        if(idx==0){
            i++;
            jumlahpercobaan = jumlahpercobaan+1;
        }
        if(assignedNumber[idx]== listhuruf.get(idx).size() && (idx!=last && i<listhuruf.get(last).size())) {
            permutasi();
            jumlahpercobaan = jumlahpercobaan+1;
        }
    }
    
    public void permute(){
        /* melakukan permutasi satu-satu dengan mengecek setiap huruf ke belakang */
        int A = pjgKata-1;
        idx = idx + A ;
        idx = idx % pjgKata;
        if(idx==A){
            i--;
        }
        if(i>=listhuruf.get(idx).size()){
            permute();
            jumlahpercobaan = jumlahpercobaan+1;
        }else{
            value = solusiAngka.get(listhuruf.get(idx).get(i))+1;
            assignedNumber[idx]--;
            char huruf = listhuruf.get(idx).get(i);
            if(jmlAssigned.get(huruf)!=1){
                int jmlAssignedTiapHuruf = jmlAssigned.get(huruf);
                jmlAssigned.put(huruf, jmlAssignedTiapHuruf - 1);
                jumlahpercobaan = jumlahpercobaan+1;
            }else {
                solusiAngka.remove(huruf);
                int jmlAssignedTiapHuruf = jmlAssigned.get(huruf);
                jmlAssigned.put(huruf,jmlAssignedTiapHuruf-1);
                jumlahpercobaan = jumlahpercobaan+1;
            }
        }
        if(idx==A){
            permute();
            jumlahpercobaan = jumlahpercobaan+1;
        }
    }


    public void calculate() {
        /* proses pencarian solusi dan assign nilai ke huruf */
        value = 0;
        i = 0;
        idx = 0;
        while(!isSolved()) {
            if (idx == pjgKata - 1) {
                int A = listAngkaBoleh.get(i);
                for (int b = 0; b < pjgKata - 1; b++) {
                    if (i < listhuruf.get(b).size()) {
                        A += solusiAngka.get(listhuruf.get(b).get(i));
                        jumlahpercobaan = jumlahpercobaan+1;
                    }
                }
                char huruf = listhuruf.get(idx).get(i);
                listAngkaBoleh.set(i + 1, A / 10);
                value = A % 10;
                if ((value==0 && isFirstChar(huruf)) || sudahAda(huruf) || isInkonsistenValue(huruf)) {
                    listAngkaBoleh.set(i + 1, 0);
                    permute();
                    jumlahpercobaan = jumlahpercobaan+1;
                }else {
                    solusiAngka.put(huruf,value);
                    assignedNumber[idx]++;
                    int jmlAssignedTiapHuruf = jmlAssigned.get(huruf);
                    jmlAssigned.put(huruf,jmlAssignedTiapHuruf+1);
                    permutasi();
                    jumlahpercobaan = jumlahpercobaan+1;
                }
            }else {
                char huruf = listhuruf.get(idx).get(i);
                while ((value==0 && isFirstChar(huruf)) || sudahAda(huruf) || isInkonsistenValue(huruf) && value <= 9) {
                    value++;
                    jumlahpercobaan = jumlahpercobaan+1;
                }
                if (value > 9) {
                    permute();
                    jumlahpercobaan = jumlahpercobaan+1;
                }else{
                    solusiAngka.put(huruf,value);
                    assignedNumber[idx]++;
                    int jmlAssignedTiapHuruf = jmlAssigned.get(huruf);
                    jmlAssigned.put(huruf,jmlAssignedTiapHuruf+1);
                    permutasi();
                    jumlahpercobaan = jumlahpercobaan+1;
                }
            }
        }
    }

    public Cryptarithmetic(int pjgKata){
        /* membuat list of character */
        int i = 0;
        listhuruf = new ArrayList<>();
        for(i = 0; i<pjgKata; i++){
            listhuruf.add(new ArrayList<Character>());
        }
        listAngkaBoleh = new ArrayList<>();
        assignedNumber = new int[pjgKata];
        solusiAngka = new Hashtable<>();
        jmlAssigned = new Hashtable<>();
        for(i=0; i<pjgKata; i++){
            assignedNumber[i]=0;
        }
        this.pjgKata = pjgKata;
    }

    public void execute(){
        /* mencetak hasil kalkulasi ke layar*/
        System.out.println(" ");
        int maxlist = listhuruf.get(0).size();
        for (int i = 1;i<listhuruf.size();i++){
            if(listhuruf.get(i).size()>maxlist){
                maxlist = listhuruf.get(i).size();
            }
        }
        for(int i=0;i<pjgKata-1;i++){
            for(int k = listhuruf.get(i).size(); k<maxlist;k++) {
                System.out.print(" ");
            }
            for (int j=listhuruf.get(i).size()-1;j>=0;j--){
                char huruf = listhuruf.get(i).get(j);
                System.out.print(solusiAngka.getOrDefault(huruf,-1));
            }
            System.out.println("");
        }
        System.out.println("-------- +");
        for (int j=listhuruf.get(pjgKata-1).size()-1;j>=0;j--){
            char huruf = listhuruf.get(pjgKata-1).get(j);
            System.out.print(solusiAngka.getOrDefault(huruf,-1));
        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Jumlah percobaan: "+jumlahpercobaan);
    }
}

