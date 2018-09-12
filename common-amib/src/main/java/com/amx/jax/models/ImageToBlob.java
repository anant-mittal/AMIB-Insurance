package com.amx.jax.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

public class ImageToBlob
{
	public static java.sql.Blob convertFileContentToBlob(MultipartFile mFile) throws IOException, SQLException
	{
		Blob blob = null;
		File file = convert(mFile);
		byte[] fileContent = new byte[(int) file.length()];
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(file);
			inputStream.read(fileContent);
			blob.setBytes(1, fileContent);
		}
		catch (IOException e)
		{
			throw new IOException("Unable to convert file to byte array. " + e.getMessage());
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
		return blob;
	}

	public static File convert(MultipartFile file)
	{
		File convFile = new File(file.getOriginalFilename());
		try
		{
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convFile;
	}

	public static byte[] convertFileContentToBlobNew(MultipartFile mFile) throws IOException
	{
		// create file object
		File file = convert(mFile);
		// initialize a byte array of size of the file
		byte[] fileContent = new byte[(int) file.length()];
		FileInputStream inputStream = null;
		try
		{
			// create an input stream pointing to the file
			inputStream = new FileInputStream(file);
			// read the contents of file into byte array
			inputStream.read(fileContent);
		}
		catch (IOException e)
		{
			throw new IOException("Unable to convert file to byte array. " + e.getMessage());
		}
		finally
		{
			// close input stream
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
		return fileContent;
	}

}
