package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.service.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class GameZoneController {
    private final GameService service = new GameService();
    private BorderPane root;
    private TableView<VideoGame> gameTable;
    private ObservableList<VideoGame> gameList;

    public BorderPane getView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Cabecera elegante
        VBox headerPane = new VBox();
        headerPane.setId("header-pane");
        Label titleLabel = new Label("GAMEZONE 🎮");
        titleLabel.setId("header-title");
        Label subtitleLabel = new Label("Consola de Control e Inventario de Videojuegos");
        subtitleLabel.setId("header-subtitle");
        headerPane.getChildren().addAll(titleLabel, subtitleLabel);
        root.setTop(headerPane);
        
        // Barra de menú lateral con botones
        VBox menuBox = createMenuButtons();
        root.setLeft(menuBox);
        
        // Tabla para listar juegos
        gameTable = new TableView<>();
        setupGameTable();
        
        // Añadir margen a la tabla para que no se pegue al sidebar
        BorderPane.setMargin(gameTable, new Insets(15, 0, 0, 15));
        root.setCenter(gameTable);
        
        refreshGameList();
        return root;
    }

    private VBox createMenuButtons() {
        Button btnAdd = new Button("➕ Agregar Juego");
        Button btnList = new Button("📋 Listar Todos");
        Button btnSearchTitle = new Button("🔍 Buscar por Título");
        Button btnSearchPlatform = new Button("🎮 Buscar por Plataforma");
        Button btnSell = new Button("💰 Realizar Venta");
        Button btnShowSales = new Button("📊 Mostrar Ventas");
        Button btnUpdate = new Button("✏️ Actualizar Juego");
        Button btnDelete = new Button("❌ Eliminar Juego");
        Button btnExit = new Button("🚪 Salir");

        btnAdd.setOnAction(e -> showAddGameDialog());
        btnList.setOnAction(e -> refreshGameList());
        btnSearchTitle.setOnAction(e -> searchByTitle());
        btnSearchPlatform.setOnAction(e -> searchByPlatform());
        btnSell.setOnAction(e -> sellGame());
        btnShowSales.setOnAction(e -> showSales());
        btnUpdate.setOnAction(e -> updateGame());
        btnDelete.setOnAction(e -> deleteGame());
        btnExit.setOnAction(e -> System.exit(0));

        VBox menu = new VBox(btnAdd, btnList, btnSearchTitle, btnSearchPlatform,
                btnSell, btnShowSales, btnUpdate, btnDelete, btnExit);
        menu.setId("sidebar");
        
        for (Button b : new Button[]{btnAdd, btnList, btnSearchTitle, btnSearchPlatform,
                btnSell, btnShowSales, btnUpdate, btnDelete, btnExit}) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.getStyleClass().add("sidebar-button");
        }
        btnExit.setId("btn-exit");
        
        return menu;
    }

    private void setupGameTable() {
        TableColumn<VideoGame, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<VideoGame, Double> colPrice = new TableColumn<>("Precio Base");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<VideoGame, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<VideoGame, String> colPlatform = new TableColumn<>("Plataforma");
        colPlatform.setCellValueFactory(new PropertyValueFactory<>("platform"));
        TableColumn<VideoGame, String> colExtra = new TableColumn<>("Detalle Extra");
        colExtra.setCellValueFactory(cellData -> {
            VideoGame g = cellData.getValue();
            if (g instanceof DigitalVideoGame) {
                return new javafx.beans.property.SimpleStringProperty("Tamaño: " + ((DigitalVideoGame) g).getSizeGB() + " GB");
            } else if (g instanceof PhysicalVideoGame) {
                return new javafx.beans.property.SimpleStringProperty("Condición: " + ((PhysicalVideoGame) g).getCondition());
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        TableColumn<VideoGame, Double> colFinal = new TableColumn<>("Precio Final");
        colFinal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().calculateFinalPrice()).asObject());

        gameTable.getColumns().addAll(colTitle, colPrice, colStock, colPlatform, colExtra, colFinal);
        gameList = FXCollections.observableArrayList();
        gameTable.setItems(gameList);
    }

    private void refreshGameList() {
        gameList.setAll(service.listAllGames());
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAddGameDialog() {
        Dialog<VideoGame> dialog = new Dialog<>();
        dialog.setTitle("Agregar Videojuego");
        dialog.setHeaderText("Complete los datos");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Digital", "Físico");
        typeCombo.setValue("Digital");

        TextField titleField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();
        TextField platformField = new TextField();
        TextField extraField = new TextField(); // sizeGB o condición

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Título:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Plataforma:"), 0, 4);
        grid.add(platformField, 1, 4);
        grid.add(new Label("Digital: Tamaño (GB) / Físico: Condición (nuevo/usado):"), 0, 5);
        grid.add(extraField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String title = titleField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());
                    String platform = platformField.getText();
                    String extra = extraField.getText();
                    if ("Digital".equals(typeCombo.getValue())) {
                        double size = Double.parseDouble(extra);
                        return new DigitalVideoGame(title, price, stock, platform, size);
                    } else {
                        return new PhysicalVideoGame(title, price, stock, platform, extra);
                    }
                } catch (Exception e) {
                    showAlert("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<VideoGame> result = dialog.showAndWait();
        result.ifPresent(game -> {
            try {
                service.addGame(game);
                refreshGameList();
                showAlert("Éxito", "Juego agregado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void searchByTitle() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Buscar por Título");
        td.setHeaderText("Ingrese el título exacto (no sensible a mayúsculas)");
        Optional<String> result = td.showAndWait();
        result.ifPresent(title -> {
            VideoGame g = service.searchByTitle(title);
            if (g != null) {
                gameList.setAll(g);
            } else {
                showAlert("No encontrado", "No hay juego con título: " + title, Alert.AlertType.WARNING);
            }
        });
    }

    private void searchByPlatform() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Buscar por Plataforma");
        td.setHeaderText("Plataforma (ej: PC, PS5, Xbox)");
        Optional<String> result = td.showAndWait();
        result.ifPresent(platform -> {
            List<VideoGame> games = service.searchByPlatform(platform);
            if (!games.isEmpty()) {
                gameList.setAll(games);
            } else {
                showAlert("Sin resultados", "No hay juegos en la plataforma: " + platform, Alert.AlertType.WARNING);
            }
        });
    }

    private void sellGame() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Venta");
        GridPane grid = new GridPane();
        grid.add(new Label("Título:"), 0, 0);
        TextField titleField = new TextField();
        grid.add(titleField, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1);
        TextField qtyField = new TextField();
        grid.add(qtyField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) return new String[]{titleField.getText(), qtyField.getText()};
            return null;
        });
        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            try {
                String title = data[0];
                int qty = Integer.parseInt(data[1]);
                double total = service.sellGame(title, qty);
                refreshGameList();
                showAlert("Venta realizada", "Total: $" + total, Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error en venta", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showSales() {
        List<Sale> sales = service.getAllSales();
        if (sales.isEmpty()) {
            showAlert("Ventas", "No hay ventas registradas", Alert.AlertType.INFORMATION);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Sale s : sales) {
            sb.append(s.toString()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historial de Ventas");
        alert.setHeaderText(null);
        alert.setContentText(sb.toString());
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }

    private void updateGame() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Actualizar Juego");
        td.setHeaderText("Ingrese el título del juego a actualizar");
        Optional<String> result = td.showAndWait();
        result.ifPresent(oldTitle -> {
            VideoGame old = service.searchByTitle(oldTitle);
            if (old == null) {
                showAlert("Error", "Juego no encontrado", Alert.AlertType.ERROR);
                return;
            }
            // Mejor: un diálogo de edición simple
            Dialog<VideoGame> editDialog = new Dialog<>();
            editDialog.setTitle("Editar " + oldTitle);
            GridPane grid = new GridPane();
            TextField newTitle = new TextField(old.getTitle());
            TextField newPrice = new TextField(String.valueOf(old.getPrice()));
            TextField newStock = new TextField(String.valueOf(old.getStock()));
            TextField newPlatform = new TextField(old.getPlatform());
            grid.add(new Label("Título:"),0,0); grid.add(newTitle,1,0);
            grid.add(new Label("Precio:"),0,1); grid.add(newPrice,1,1);
            grid.add(new Label("Stock:"),0,2); grid.add(newStock,1,2);
            grid.add(new Label("Plataforma:"),0,3); grid.add(newPlatform,1,3);
            editDialog.getDialogPane().setContent(grid);
            editDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            editDialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        if (old instanceof DigitalVideoGame) {
                            double size = ((DigitalVideoGame) old).getSizeGB();
                            return new DigitalVideoGame(newTitle.getText(), Double.parseDouble(newPrice.getText()),
                                    Integer.parseInt(newStock.getText()), newPlatform.getText(), size);
                        } else {
                            String cond = ((PhysicalVideoGame) old).getCondition();
                            return new PhysicalVideoGame(newTitle.getText(), Double.parseDouble(newPrice.getText()),
                                    Integer.parseInt(newStock.getText()), newPlatform.getText(), cond);
                        }
                    } catch (Exception e) {
                        showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
                        return null;
                    }
                }
                return null;
            });
            Optional<VideoGame> updated = editDialog.showAndWait();
            updated.ifPresent(game -> {
                try {
                    service.updateGame(oldTitle, game);
                    refreshGameList();
                    showAlert("Éxito", "Juego actualizado", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        });
    }

    private void deleteGame() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Eliminar Juego");
        td.setHeaderText("Ingrese el título del juego a eliminar");
        Optional<String> result = td.showAndWait();
        result.ifPresent(title -> {
            try {
                service.deleteGame(title);
                refreshGameList();
                showAlert("Eliminado", "Juego eliminado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
}
