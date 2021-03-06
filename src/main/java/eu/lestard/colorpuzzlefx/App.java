package eu.lestard.colorpuzzlefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import eu.lestard.colorpuzzlefx.ai.BogoSolver;
import eu.lestard.colorpuzzlefx.ai.Solver;
import eu.lestard.colorpuzzlefx.view.MainView;
import eu.lestard.colorpuzzlefx.view.MainViewModel;
import eu.lestard.easydi.EasyDI;

public class App extends Application {

    public static void main(String...args){
        App.launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("ColorPuzzleFX");

        EasyDI context = new EasyDI();
        context.bindInterface(Solver.class, BogoSolver.class);

        MvvmFX.setCustomDependencyInjector(context::getInstance);


        final ViewTuple<MainView, MainViewModel> viewTuple = FluentViewLoader.fxmlView(MainView.class).load();

        stage.setScene(new Scene(viewTuple.getView()));
        stage.sizeToScene();
        stage.show();

    }
}
