import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para representar um usuário do sistema.
 */
public class User {
    private String name;  // Nome do usuário
    private String email; // Email do usuário
    private String id;    // ID único do usuário
    private String status; // Status do usuário

    private static final String FILE_NAME = "users.txt"; // Nome do arquivo que armazena os dados dos usuários

    // Construtor padrão
    public User(String name, String email, String status) {
        this.name = name;
        this.email = email;
        this.id = generateID(email); // ID único baseado no email
        this.status = status != null ? status : "ENVIAR CONVITE"; // Atribui um status padrão se não fornecido

        saveToDatabase(); // Salva o usuário no "banco de dados" (arquivo)
    }

    // Construtor para criar um usuário a partir de dados do arquivo
    public User(String id, String name, String email, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    // Método para salvar o usuário no arquivo
    private void saveToDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(this.id + ";" + this.name + ";" + this.email + ";" + this.status);
            writer.newLine(); // Adiciona uma nova linha após o registro
            System.out.println("Usuário " + this.name + " salvo no arquivo com ID: " + this.id);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage()); // Exibe mensagem de erro em caso de falha
        }
    }

    // Método para buscar um usuário por ID ou email
    public static User search(String identifier) {
        List<User> users = loadUsersFromDatabase(); // Carrega todos os usuários do arquivo
        for (User user : users) {
            if (user.id.equals(identifier) || user.email.equals(identifier)) {
                return user; // Retorna o usuário encontrado
            }
        }
        System.out.println("Nenhum usuário encontrado com identificador: " + identifier);
        return null; // Retorna null se não encontrar o usuário
    }

    // Método privado para carregar usuários do arquivo
    private static List<User> loadUsersFromDatabase() {
        List<User> users = new ArrayList<>(); // Lista para armazenar usuários
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) { // Lê o arquivo linha por linha
                String[] parts = line.split(";"); // Divide cada linha em partes
                if (parts.length == 4) { // Verifica se a linha contém todos os dados necessários
                    users.add(new User(parts[0], parts[1], parts[2], parts[3])); // Cria o usuário a partir dos dados lidos
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage()); // Exibe mensagem de erro em caso de falha
        }
        return users; // Retorna a lista de usuários
    }

        // Sobrecarga do método sendInvite para buscar por ID
        public static void sendInvite(String identifier) {
            User user = search(identifier); // Busca usuário por ID ou email
            if (user != null) {
                user.status = "CONVITE ENVIADO"; // Atualiza o status do usuário
                updateUserInDatabase(user); // Atualiza o arquivo com o novo status
                System.out.println("Convite enviado para: " + user.name + ", Status: " + user.status);
            }
        }
    
        // Sobrecarga do método sendInvite para buscar por email
        public static void sendInvite(User user) {
            user.status = "CONVITE ENVIADO"; // Atualiza o status do usuário
            updateUserInDatabase(user); // Atualiza o arquivo com o novo status
            System.out.println("Convite enviado para: " + user.name + ", Status: " + user.status);
        }
    
        // Método para atualizar o usuário no arquivo após alteração de status
        private static void updateUserInDatabase(User updatedUser) {
            List<User> users = loadUsersFromDatabase(); // Carrega todos os usuários do arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (User user : users) {
                    if (user.id.equals(updatedUser.id)) {
                        writer.write(updatedUser.id + ";" + updatedUser.name + ";" + updatedUser.email + ";" + updatedUser.status);
                    } else {
                        writer.write(user.id + ";" + user.name + ";" + user.email + ";" + user.status);
                    }
                    writer.newLine(); // Adiciona uma nova linha após o registro
                }
            } catch (IOException e) {
                System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            }
        }

    // Método para gerar um ID único baseado no email
    private static String generateID(String email) {
        return Integer.toString(email.hashCode()); // Gera ID simples usando o hash do email
    }
    
    // Método para enviar convite para o usuário

    // Método principal para exemplo de uso
    public static void main(String[] args) {
        // 1. Criar um novo usuário
        User user1 = new User("felipe", "fmartiniano@tv1.com.br", null); // Salva o usuário no arquivo

        // 2. Enviar convite para o usuário pelo email 
        // User.sendInvite("jane@example.com");

        // 2.1. Enviar convite para o usuário pelo ID
        // User.sendInvite(user1.id);
        
        // 3. Buscar o usuário pelo email
        User foundUser = User.search("jane@example.com"); // Busca usuário pelo email
        if (foundUser != null) {
            System.out.println("Usuário encontrado: " + foundUser.name + ", ID: " + foundUser.id + ", Status: " + foundUser.status);
        }

        // 3.1. Buscar o usuário pelo ID
        User foundById = User.search(user1.id); // Busca usuário pelo ID
        if (foundById != null) {
            System.out.println("Usuário encontrado pelo ID: " + foundById.name + ", Email: " + foundById.email + ", Status: " + foundById.status);
        }
    }
}
