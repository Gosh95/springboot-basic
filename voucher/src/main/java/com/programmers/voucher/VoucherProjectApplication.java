package com.programmers.voucher;

import com.programmers.voucher.config.ApplicationMessages;
import com.programmers.voucher.entity.voucher.Voucher;
import com.programmers.voucher.service.customer.CustomerInitializer;
import com.programmers.voucher.service.voucher.VoucherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class VoucherProjectApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(VoucherProjectApplication.class);
		VoucherService voucherService = applicationContext.getBean(VoucherService.class);
		voucherService.openStorage();

		CustomerInitializer blacklist = applicationContext.getBean(CustomerInitializer.class);
		blacklist.loadCustomers();

		System.out.println("====== [ BLACKLIST ] ======");
		blacklist.readCustomers().forEach(System.out::println);
		System.out.println("===========================");

		ApplicationMessages applicationMessages = applicationContext.getBean(ApplicationMessages.class);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println(applicationMessages.getIntroMessage());
			String command = br.readLine();
			switch (command) {
				case "exit":
					voucherService.closeStorage();
					System.exit(0);
					break;

				case "create":
					System.out.print(applicationMessages.getRequireName());
					String voucherName = br.readLine();
					System.out.print(applicationMessages.getRequireType());
					String voucherType = br.readLine();

					Voucher createdVoucher;
					switch (voucherType) {
						case "Fixed":
							createdVoucher = voucherService.create(voucherName, Voucher.type.FIXED, 1000);
							break;

						case "Percent":
							createdVoucher = voucherService.create(voucherName, Voucher.type.PERCENT, 0.1);
							break;

						default:
							System.out.println(applicationMessages.getFallback());
							createdVoucher = voucherService.create(voucherName, Voucher.type.FIXED, 1000);
					}
					System.out.printf("Created %s%n", createdVoucher.toString());
					break;

				case "list":
					System.out.println("======= [ VOUCHERS ] =======");
					voucherService.listAll().forEach(System.out::println);
					System.out.println("============================");
					break;
			}
		}
	}

}
