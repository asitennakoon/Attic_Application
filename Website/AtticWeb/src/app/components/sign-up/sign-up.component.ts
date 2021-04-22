import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IAccount } from 'src/app/Interfaces/IAccount';
import { LoginService } from 'src/app/Services/login.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  /* This component is for the signup functionality. */

  public accounts: IAccount[] = [];
  user: IAccount;

  public credentialsValid: boolean = true;
  public error = ""
  public showContent = false;

  public signupForm: FormGroup;


  constructor(private loginService: LoginService, private router: Router) {
    // subscribe to the login service and get all accounts.
    this.loginService.getAccounts().subscribe(data => {
      this.accounts = data;
      // console.log(data);

    });

    // initialise the form controls with required validator(mark as required)
    this.signupForm = new FormGroup({
      'username': new FormControl('', [
        Validators.required
      ]),
      'email': new FormControl('', [
        Validators.email,
        Validators.required
      ]),
      'storeName': new FormControl('', [
        Validators.required
      ]),
      'password': new FormControl('', [
        Validators.required
      ]),
      'confirmPassword': new FormControl('', [
        Validators.required
      ])
    });


  }

  ngOnInit() {
    // console.log(this.accounts);
  }

  onSubmit(): void {
    // console.log(this.signupForm.value)

    // check the form validity using the initialised form control validators
    if (this.signupForm.valid) {
      console.log("form valid")
      // check whether the account exists and passwords is valid
      if ((!this.checkAccountExists()) && this.passwordValid()) {
        this.credentialsValid = true;
        let value = this.signupForm.value;
        let newAccount: IAccount = {
          username: value.username,
          password: value.password,
          store: value.storeName,
          email: value.email,
          /* for the current version of the website doesn't have the membership options. 
          Therefore, Platinum account is set by default.
           */
          type: "Platinum",
        }

        // console.log(newAccount);
        // add the account to firestore using the login service.
        this.loginService.addAccount(newAccount);
        // return to home page (login component)
        this.router.navigateByUrl("");
        return;

      }

      // show an error if the account already exists
      if (this.checkAccountExists()) {
        this.error = "Account already exists ! "
        this.credentialsValid = false;
        return;
      }

      // show an error if passwords doesn't match
      if (!this.passwordValid()) {
        this.error = "Passwords doesn't match ! "
        this.credentialsValid = false;
        return;
      }
    }
    // if all the email is not valid show an error
    this.error = "Invalid email address ! "
    this.credentialsValid = false;
  }

  // the method to check the existance of an account
  checkAccountExists(): boolean {
    // compare username and passwords using for each loop.
    for (let account of this.accounts) {
      console.log(account)
      if (this.signupForm.value.username == account.username
        && this.signupForm.value.password == account.password
      ) {
        console.log("account exists")

        return true;
      }

    };
    console.log("account does not exist")

    return false;
  }
  // a method to check the password validity
  passwordValid(): boolean {
    if (this.signupForm.value.password
      == this.signupForm.value.confirmPassword) {
      console.log("password match")

      return true;
    }


    return false;
  }

}
