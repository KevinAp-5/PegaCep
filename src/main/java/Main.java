import service.ViaCepService;

public class Main {
    public static void main(String[] args) {
        ViaCepService viacep = new ViaCepService();

        System.out.println(viacep.getEndereco("15995055"));
    }
}
