
import Models.DTOBJCursa;
import Service.Service;
import Services.IServices;
import Services.ServerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.ServicesImplementation;

import java.net.ConnectException;


public class StartServer  {
    private static int defaultport=55555;
    static Service getService(){
        ApplicationContext context=new ClassPathXmlApplicationContext("springServer.xml");
        Service service=context.getBean(Service.class);
        return service;
    }
    static ServicesImplementation getServices(){
        ApplicationContext context=new ClassPathXmlApplicationContext("springServer.xml");
        ServicesImplementation servicesImplementation=context.getBean(ServicesImplementation.class);
        return servicesImplementation;
    }
    public static void main(String[] args){
        Service service=getService();
        System.out.println(service.AngajatiSize());
        ServicesImplementation servicesImplementation=getServices();
        try{
        DTOBJCursa[] curse=servicesImplementation.getCurseDisp();
            for (DTOBJCursa c:curse
                 ) {
                System.out.println(c.getCapacitate());
            }
        }catch (ServerException e){
            e.printStackTrace();
        }

        ApplicationContext factory = new ClassPathXmlApplicationContext("springServer.xml");

    }

}
