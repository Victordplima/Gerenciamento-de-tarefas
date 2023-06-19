import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GerenciadorUsuarios {
    private static final String USUARIOS_PATH = "Usuarios/";
    private String nomeUsuario;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public boolean realizarLogin(Scanner scanner) {
        System.out.print("Digite o nome de usuário: ");
        nomeUsuario = scanner.nextLine();

        if (!existeUsuario(nomeUsuario)) {
            System.out.println("Usuário não encontrado. Criando novo usuário...");

            if (criarUsuario(nomeUsuario)) {
                System.out.print("Digite a senha para o novo usuário: ");
                String senha = scanner.nextLine();
                salvarSenha(nomeUsuario, senha);
                System.out.println("Usuário criado com sucesso.");
            } else {
                System.out.println("Falha ao criar a pasta do usuário.");
            }
        } else {
            System.out.print("Digite a senha: ");
            String senha = scanner.nextLine();

            while (!verificarSenha(nomeUsuario, senha)) {
                System.out.println("Senha incorreta. Tente novamente.");
                System.out.print("Digite a senha: ");
                senha = scanner.nextLine();
            }

            System.out.println("Login bem-sucedido!");
        }

        return true;
    }

    public void criarPastaUsuarios() {
        File usuariosDir = new File(USUARIOS_PATH);
        if (!usuariosDir.exists()) {
            if (usuariosDir.mkdirs()) {
                System.out.println("Pasta 'Usuarios/' criada com sucesso.");
            } else {
                System.out.println("Falha ao criar a pasta 'Usuarios/'.");
            }
        }
    }

    private boolean existeUsuario(String nomeUsuario) {
        File usuarioDir = new File(USUARIOS_PATH + nomeUsuario);
        return usuarioDir.exists();
    }

    private boolean criarUsuario(String nomeUsuario) {
        File usuarioDir = new File(USUARIOS_PATH + nomeUsuario);
        System.out.println("Caminho da pasta de usuário: " + usuarioDir.getAbsolutePath());
        return usuarioDir.mkdir();
    }

    private void salvarSenha(String nomeUsuario, String senha) {
        String usuarioPath = USUARIOS_PATH + nomeUsuario + "/senha.txt";

        try (FileWriter writer = new FileWriter(usuarioPath, StandardCharsets.UTF_8)) {
            writer.write(senha);
        } catch (IOException e) {
            System.out.println("Erro ao salvar a senha.");
            e.printStackTrace();
        }
    }

    private boolean verificarSenha(String nomeUsuario, String senha) {
        String usuarioPath = USUARIOS_PATH + nomeUsuario + "/senha.txt";
        Path path = Paths.get(usuarioPath);

        try {
            String senhaSalva = Files.readString(path);
            return senha.equals(senhaSalva);
        } catch (NoSuchFileException e) {
            System.out.println("Arquivo de senha não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao verificar a senha.");
            e.printStackTrace();
        }

        return false;
    }
}
