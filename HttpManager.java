import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class HttpManager {
    Socket socket;
    Scanner input;
    PrintWriter output;
    String startHtml;

    public HttpManager(Socket socket, String startHtml) {
        this.socket = socket;
        this.startHtml = startHtml;
        try {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        String header = input.nextLine();
        System.out.println("header ->" + header);
        String requestType = (header.split(" "))[0];
        if (requestType.equals("POST")) {
            System.out.println("PPPPPOOOOSSSTTT!!!!");
            //return;
        }
        String address = (header.split(" "))[1];
        if (address.equals("/login")) {
            System.out.println("IIIIIINNNNNN!!!!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String formVal = "notEmptyString";
            for (int i = 0; i < 100; ++i) {
                if (formVal.isEmpty()) {
                    System.out.println("i-> " + i);
                    socket.close();
                    try {
                        formVal = input.nextLine();
                    }
                    catch (Exception e) {
                    }
                    break;
                }
                formVal = input.nextLine();
                System.out.println(formVal);
            }
            System.out.println("form-> " + formVal);
            System.out.println("client's email is " + (formVal.substring(5)).replaceFirst("%40", "@"));
            return;
        }
        System.out.println("address ->" + address);
        sendFile(address);
    }

    private String getContentType(String ext) throws Exception {
        if (ext.equals("jpeg") ||
                ext.equals("jpg") ||
                ext.equals("png") ||
                ext.equals("tiff") ||
                ext.equals("webp")
                ) {
            return "image";
        }
        if (ext.equals("html") ||
                ext.equals("css") ||
                ext.equals("cmd") ||
                ext.equals("csv") ||
                ext.equals("xml") ||
                ext.equals("php")
                ) {
            return "text";
        }
        throw new Exception("Wrong Format -> " + ext);
    }

    private void sendStartPage() throws FileNotFoundException {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println();
        File htmlText = new File(startHtml);
        Scanner htmlReader = new Scanner(htmlText);
        while (htmlReader.hasNextLine()) {
            output.println(htmlReader.nextLine());
        }
        output.println();
        output.flush();
    }

    private void sendComponent(String address) throws IOException {
        address = address.substring(1);
        File file = new File(address);
        String fileType = (address.split("\\."))[1];
        String contentType;
        try {
            if (!file.exists()) {
                throw new Exception();
            }
            contentType = getContentType(fileType);
        }
        catch (Exception e) {
            System.out.println("404!!! File not Found");
            output.println("HTTP/1.0 404 Not Found");
            output.println();
            output.flush();
            output.close();
            //socket.close();
            return;
        }
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: " + contentType + "/" + fileType);
        output.println("Content-Length: " + file.length());
        output.println();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[(int) file.length()];
        int bytesRead;
        while ( (bytesRead = reader.read(buffer)) != -1 ) {
            out.write(buffer, 0, bytesRead);
        }
        output.flush();
        out.flush();
    }

    private void sendFile(String address) throws IOException {
        if (address.equals("/")) {
            sendStartPage();
        }
        else if (!address.equals("/favicon.ico")) {
            sendComponent(address);
        }
    }

}
