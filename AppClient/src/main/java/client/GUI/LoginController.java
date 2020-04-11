package client.GUI;

import Models.DTOAngajat;
import Services.IServices;
import Services.ServerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class LoginController {
    private IServices server;
    private MainController mainCtrl;
    private DTOAngajat crtAngajat;

    @FXML
    TextField TFUser;
    @FXML
    PasswordField PFPass;
    @FXML
    Button LoginButton;

    Parent mainParent;

    public void SetServer(IServices server1){
        this.server=server1;
    }

    public void SetParent(Parent parent1){
        mainParent=parent1;
    }

    public void SetMainCtrl(MainController ctrl){
        this.mainCtrl=ctrl;
    }

    public void pressCancel(ActionEvent event){
        System.exit(0);
    }

    public void setCrtAngajat(DTOAngajat crtAngajat) {
        this.crtAngajat = crtAngajat;
    }

    @FXML
    public void pressLogin(ActionEvent actionEvent){
        String user=TFUser.getText();
        String pass=PFPass.getText();
        crtAngajat=new DTOAngajat(user,pass);

        try{
            server.login(crtAngajat,mainCtrl);
            Stage stage=new Stage();
            stage.setTitle("Main Window for "+crtAngajat.getUsername());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainCtrl.logout(actionEvent);
                    System.exit(0);
                }
                });
            stage.show();
            mainCtrl.setUser(crtAngajat);
            mainCtrl.initialiazeTabels();
            mainCtrl.setCurseTabel();
            mainCtrl.setComboBox();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }catch(ServerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("An error occured : "+e.getMessage());
            alert.showAndWait();
        }
    }



}
