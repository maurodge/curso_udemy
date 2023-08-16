package services;

import java.time.LocalDate;

import entities.Contract;
import entities.Installment;

public class ContractService {

	private OnlinePaymentService onlinePaymentService;

	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}
	
	public void processContract(Contract contract, int months) {
		
		double basicQuota = contract.getTotalValue() / months;
		
		for (int i=1; i <= months; i++) {
			// datas dos pagamentos
			LocalDate dueDate = contract.getDate().plusMonths(i);
			
			// definindo o juros
			double interest = onlinePaymentService.interest(basicQuota, i);
			//definindo a taxa do cartão
			double fee = onlinePaymentService.paymentFee(basicQuota + interest);
			// valor de cada parcela (está dentro do for, o valor muda de acordo com a variação do i)
			double quota = basicQuota + interest + fee;
			
			contract.getInstallments().add(new Installment(dueDate, quota));
		}
	}
}
