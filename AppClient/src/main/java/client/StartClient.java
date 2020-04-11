package client;


import Services.IServices;
import client.GUI.LoginController;
import client.GUI.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartClient extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {

        ApplicationContext factory = new ClassPathXmlApplicationContext("springclient.xml");
        IServices server=(IServices) factory.getBean("implementation");




        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("LoginView.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.SetServer(server);




        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("MainView.fxml"));
        Parent croot=cloader.load();


        MainController mainCtrl =
                cloader.<MainController>getController();
        mainCtrl.setServer(server);

        ctrl.SetMainCtrl(mainCtrl);
        ctrl.SetParent(croot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
