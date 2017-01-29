package gt.com.itSoftware.framework.core.utils.fle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
	
	public static ZipOutputStream zos = null;
	
	public static void compressToZipFile(String fileIn, String fileOut ) throws IOException{
			if (!fileOut.toLowerCase().endsWith(".zip")){
				throw new RuntimeException("El archivo debe finalizar con el postifjo .zip");
			}
		    byte[] buf = new byte[1024];		   
	        // archivo que va a escribir.
	    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileOut));
            out.putNextEntry(new ZipEntry(fileIn));
	    	//archivo que va a leer
	        FileInputStream in = new FileInputStream(fileIn);
            // Se lee el de 1024 bytes en 1024 
	        int len = in.read(buf);
            while (len > 0) {
                out.write(buf, 0, len);
                len = in.read(buf);
            }		            
	        out.closeEntry();
	        out.flush();
	        in.close();	        
	        out.close();
	}
	
	
	
	public static void compressToGzipFile(String fileIn) throws IOException{				 
        byte[] buffer = new byte[1024];  
    	GZIPOutputStream gzos = 	new GZIPOutputStream(new FileOutputStream(fileIn+".gz"));    
    	FileInputStream in =  new FileInputStream(fileIn);
    	int len;
    	while ((len = in.read(buffer)) > 0) {
    			gzos.write(buffer, 0, len);
    	}
    	in.close();       
    	gzos.finish();
    	gzos.close();	
	}
	
	public static void uncompressGZipFile(String fileIn) throws IOException{
		if (!fileIn.toLowerCase().endsWith(".gz")){
			throw new RuntimeException("El archivo debe finalizar con el postifjo .gz");
		}
		// se lee el archivo en 1024 bytes
	    byte[] buffer = new byte[1024];	 
	    
	    GZIPInputStream archivoComprimido = new GZIPInputStream(new FileInputStream(fileIn));	    
	    String fileOut = fileIn.replace(".gz", ""); 
    	FileOutputStream archivoDescomprimido = new FileOutputStream(fileOut);
 
        int len;
        // se traslada del archivo comprimido al descomprimido la informacion.
        // solo es de utilidad pra determinar el tamaño real del archivo
        while ((len = archivoComprimido.read(buffer)) > 0) {
        	archivoDescomprimido.write(buffer, 0, len);
        } 
        archivoComprimido.close();        
        
    	archivoDescomprimido.close();
	}
	
	 

	  
	public static int splitFile2 (String file, int longitudEnBytes, boolean eliminarPrincipal) throws IOException{
		    byte[] buffer = new byte[longitudEnBytes];  
		    // Archivo que se va a leer.    
		    File  archvioFisico = new File(file);
	        FileInputStream in =  new FileInputStream(archvioFisico);
	        //archivo que se va a escribir
	        FileOutputStream copia = null;        
	        int contador = 0;
	    	int len;
	    	while ((len = in.read(buffer)) > 0) {
	    			contador++;
	    			copia = new FileOutputStream(file+"."+contador,false);
	    			copia.write(buffer, 0, len);
	    			copia.flush();
	    	    	copia.close();	
	    	}
	    	in.close();       
	    	if (eliminarPrincipal)
	    		archvioFisico.delete();
	    	return contador;
	}
	
	
	public static int splitFile (String file, int longitudEnMegaBytes) throws IOException{
		 	
			File archivo = new File(file);
			FileInputStream lectura = new FileInputStream(archivo);
			int contador = 1;
			byte[] buffer = new byte[1024];
			int bytesLeidos = 0;
			long tamanioArchivo = 0;
			FileOutputStream parte = new FileOutputStream(file+"."+contador,false);
			while ((bytesLeidos = lectura.read(buffer)) > 0){
				// Se incrementa el tamaño en bytes.
				tamanioArchivo+=bytesLeidos;
				if (tamanioArchivo >= (longitudEnMegaBytes*1048576)){
					parte.flush();
					parte.close();
					contador++;
					// se reinicia el conte de bytes.
					tamanioArchivo = bytesLeidos;
					parte = new FileOutputStream(file+"."+contador,false);
				}
				parte.write(buffer,0,bytesLeidos);				
			}
			parte.flush();
	    	parte.close();
	    	lectura.close();    
			return contador;
			
	}

	public static int splitFile (String file, int longitudEnMegaBytes, boolean eliminarPincipal) throws IOException{
	 	
		File archivo = new File(file);
		FileInputStream lectura = new FileInputStream(archivo);
		int contador = 1;
		byte[] buffer = new byte[1024];
		int bytesLeidos = 0;
		long tamanioArchivo = 0;
		FileOutputStream parte = new FileOutputStream(file+"."+contador,false);
		while ((bytesLeidos = lectura.read(buffer)) > 0){
			// Se incrementa el tamaño en bytes.
			tamanioArchivo+=bytesLeidos;
			if (tamanioArchivo >= (longitudEnMegaBytes*1048576)){
				parte.flush();
				parte.close();
				contador++;
				// se reinicia el conte de bytes.
				tamanioArchivo = bytesLeidos;
				parte = new FileOutputStream(file+"."+contador,false);
			}
			parte.write(buffer,0,bytesLeidos);				
		}
		parte.flush();
    	parte.close();
    	lectura.close();    
    	if (eliminarPincipal)
    		archivo.delete();
		return contador;
		
}
	
	public static void joinFile (String file, int numeroDePartes, boolean eliminarPartes) throws IOException{
	    byte[] buffer = new byte[1024];
	    File archivoLeido = null;
	    // Archivo que se va a leer.    
        FileInputStream in = null;
        //archivo que se va a escribir
        FileOutputStream copia = new FileOutputStream(file,false);
        // longitud de bytes leidos
    	int len;    	
    	// para cada una de las partes. 
    	for (int i = 1; i <= numeroDePartes ; i++) {
    		archivoLeido = new File(file+"."+i);
    		in =  new FileInputStream(archivoLeido);
    		while ((len = in.read(buffer)) > 0) {
    			copia.write(buffer, 0, len);    				
	    	}
	    	in.close();
	    	if (eliminarPartes)
	    		archivoLeido.delete();
		}
    	copia.flush();
    	copia.close();
    	
	}
	
	/**
	 * Permite eliminar un directorio y su contendido de forma recursiva.
	 * 
	 * @param folder 	El directorio que se desea eliminar.
	 */
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { // si el directorio está vacio.
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	
	
	
	
	public static void comprimirDirectorio(String pFileName, String pRutaArchivoSalida) throws IOException, FileNotFoundException  
	{  
		if (!pRutaArchivoSalida.toLowerCase().endsWith(".zip")){
			throw new RuntimeException("El nombre del archivo debe finalizar con el postifjo .zip");
		}
		
		File file = new File(pFileName);
		zos = new ZipOutputStream(new FileOutputStream(pRutaArchivoSalida));
		compresionRecursivaArchivos(file,"",pFileName);
		zos.close();  
	}  

	
	
	private static void compresionRecursivaArchivos(File pFile, String pRutaArchivo, String pRutaOrigen) throws IOException, FileNotFoundException  
	{
		//Si es directorio se itera recursivamente en busca de archivos
		if (pFile.isDirectory())
		{  
			String[] fileNames = pFile.list();  
		
			if (fileNames != null)
			{  
				for (int i=0; i<fileNames.length; i++)
				{   
					compresionRecursivaArchivos(new File(pFile, fileNames[i]),fileNames[i],pRutaOrigen);  
				}  
			}  
		}
		//Si es archivo se comprime
		else
		{  
			byte[] buf = new byte[1024];  
			int len;  

			//Esta funcionalidad se utiliza para que los directorios del zip empiecen desde el directorio de compresión
			//y no todo el directorio raiz que contenia el directorio original
			//Ejemplo: ruta original c:\informacion\carperta_archivos
			//la variable ruta tendra entonces carpeta_archivos y todos los directorios hijos
			File fileOrigen = new File(pRutaOrigen);
			String ruta = pFile.toString();
			ruta = ruta.replace(fileOrigen.toString(), "");
			File rutaConvertida = new File(ruta);
			if(rutaConvertida.toString().trim().length()>1)
			{
				String primerCaracter = rutaConvertida.toString().trim().substring(0, 1);
				if(primerCaracter.equals("\\") || primerCaracter.equals("/"))
				{
					ruta = rutaConvertida.toString().trim().substring(1, rutaConvertida.toString().trim().length());
				}
			}
			
			//Compresion del archivo en la ruta indicada, en este caso la ruta empieza
			//desde el directorio de compresión hacia los directorios hijos
			ZipEntry zipEntry = new ZipEntry(ruta);  
    
			FileInputStream fin = new FileInputStream(pFile);  
			BufferedInputStream in = new BufferedInputStream(fin);  
			zos.putNextEntry(zipEntry);  

          
			while ((len = in.read(buf)) >= 0)
			{  
				zos.write(buf, 0, len);  
			}
	
			in.close();  
			zos.closeEntry();  
		}  
	}
	
	
	
	public static void main(String[] args) {
		
		/*try {*/
			//FileUtils.joinFile("d:/split/sqldeveloper-2.1.1.64.45.zip",3,true);
			//System.out.println(FileUtils.splitFile("d:/split/sqldeveloper-2.1.1.64.45.zip",50,true));
			//FileUtils.compressToGzipFile("d:/split/sqldeveloper-2.1.1.64.45.zip");
			
			//String directorio = "C:/INSTSOFT/PruebasZip/entrada/Preguntas.docx";
			//String directorio = "C:\\PruebasZip\\entrada\\";
			
			/*File f = new File(directorio);
			File[] ficheros = f.listFiles();

			for (int i=0;i<ficheros.length;i++)
			{
				System.out.println(ficheros[i].getName());
			}*/
			
		/*} catch (IOException e) {
			e.printStackTrace();
		}*/
			
		/*
		//Ejemplo de la compresion de directorios
		String directorio = "C:/PruebasZip/entrada/";
		try {
			comprimirDirectorio(directorio,"c:/Desarrollo_EGRT/DirectorioZip.zip");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
