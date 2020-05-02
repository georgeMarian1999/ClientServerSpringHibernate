package client.GUI;

import Models.*;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainController extends UnicastRemoteObject implements Initializable,IObserver {

    ObservableList<DTOBJCursa> Curse= FXCollections.observableArrayList();
    ObservableList<DTOBJPartCapa> Participanti=FXCollections.observableArrayList();

    private DTOAngajat crtAngajat;
    private IServices server;

    @FXML
    Button LogoutButton;
    @FXML
    TableView<DTOBJCursa> TabelCurse;
    @FXML
    TableColumn<DTOBJCursa,Integer> IdCol;
    @FXML
    TableColumn<DTOBJCursa,Integer> CapCol;
    @FXML
    TableColumn<DTOBJCursa,Integer> NrCol;
    @FXML
    Button clearsearch;
    @FXML
    TextField TFSearch;
    @FXML
    Button SearchByTeam;
    @FXML
    TableView<DTOBJPartCapa> TabelPart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> IDPart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> NumePart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> CapacitatePart;
    @FXML
    ComboBox<String> TeamBox;
    @FXML
    Button InscButton;
    @FXML
    ComboBox<Integer> CapBox;
    @FXML
    TextField TFNumePart;
    @FXML
    TextField TFNumeEchipa;



    ObservableList<DTOAngajat> others=FXCollections.observableArrayList();

    public  MainController() throws RemoteException {
    }

    public void setServer(IServices server1){
        this.server=server1;
    }

    public void setUser(DTOAngajat crtAngajat){
        this.crtAngajat=crtAngajat;
    }

    @FXML
    public void handleclear(){
        TFSearch.setText("");
        TFNumePart.setText("");
        TFNumeEchipa.setText("");

    }

    public void AngajatSubmitted(DTOBJCursa[] result) throws ServerException {
        Platform.runLater(()->{
            System.out.println("S-a apelat AngajatSubmitted din MainCtrl");
            AddNewDataCurse(result);

        });




    }
    public void AddNewDataCurse(DTOBJCursa [] source){
        this.Curse.clear();
        for(DTOBJCursa c:source){
            this.Curse.add(c);
        }
        this.TabelCurse.setItems(this.Curse);
    }
    @FXML
    public void handleLogout(ActionEvent actionEvent){
        logout(actionEvent);
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }
    @FXML
    public void handlesearch(){
        try{
            this.TabelPart.getItems().clear();
            String team=TFSearch.getText();
            DTOBJPartCapa[] result=this.server.searchByTeam(team);
            for(DTOBJPartCapa part : result){
                Participanti.add(part);
            }
            this.TabelPart.setItems(Participanti);
        }catch (ServerException e){
            System.out.println("Error when searching "+e);
        }
    }
    public void logout(ActionEvent actionEvent) {
        try {
            server.logout(crtAngajat, this);
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (ServerException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void Inscriere()throws ServerException{
        System.out.println("se apeleaza Inscriere");
        String numePart=TFNumePart.getText();
        String numeEchipa=TFNumeEchipa.getText();
        int capacitate= CapBox.getValue();
        System.out.println(numePart+" "+numeEchipa+" "+capacitate);
        DTOInfoSubmit submit=new DTOInfoSubmit(crtAngajat,capacitate,numePart,numeEchipa);
        this.server.submitInscriere(submit);
    }

    @FXML
    public void handlesubmit(){
        System.out.println("Se apeleaza Inscriere");
        try{
            Inscriere();
        }catch (ServerException e){
            System.out.println("Error when submitting from MainCtrl"+e);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
    public void initialiazeTabels(){
        IdCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("id"));
        CapCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("capacitate"));
        NrCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("Nrinscrisi"));
        IDPart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("id"));
        NumePart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("Nume"));
        CapacitatePart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("capactiate"));
    }
    public void setCurseTabel() {


                try{
                    this.TabelCurse.getItems().clear();
                    DTOBJCursa[] curse=this.server.getCurseDisp();
                    for(DTOBJCursa c:curse){
                        Curse.add(c);
                    }
                    TabelCurse.setItems(Curse);
                }catch (ServerException e){
                    System.out.println("Error when setting CurseTabel"+e);
                }
            }



    public void setComboBox() {
        ObservableList<Integer> tipuriCurse=FXCollections.observableArrayList();
        for(DTOBJCursa c : this.Curse){
            tipuriCurse.add(c.getCapacitate());
        }
        CapBox.setItems(tipuriCurse);
    }

    public void setTeamBox() {
        ObservableList<String> teams=FXCollections.observableArrayList();
        try {
            String[] team = server.getAllTeams();
            teams.addAll(Arrays.asList(team));
            TeamBox.setItems(teams);
        }catch (ServerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error getting all the teams");
            alert.setContentText("An error occured : "+e.getMessage());
            alert.showAndWait();
        }
    }
}
