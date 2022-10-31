import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EmailValidator } from 'src/app/core/shared/validators/email-validator.component';
import { PasswordValidator } from 'src/app/core/shared/validators/password-validator.component';
import { User } from '../models/user';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public carregando: boolean = false;
  public savedRoute: string = "";

  constructor(
    private fb: FormBuilder,
    private sessionService: SessionService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.savedRoute = this.route.snapshot.params['savedRoute'];
    sessionStorage.clear();
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(80)]],
      nick: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100),]],
      confirmEmail: ['', [Validators.required, Validators.email, Validators.maxLength(100), EmailValidator('email')]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(80)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(80), PasswordValidator('password')]]
    });
  }

  createUser() {
    sessionStorage.clear();
    if (this.form.dirty && this.form.valid) {
      this.carregando = true;
      let form = Object.assign({}, new User, this.form.value);

      let p = new User;
      p.name = form.name;
      p.nick = form.nick;
      p.email = form.email;
      p.password = form.password;

      this.sessionService.createUser(p)
        .subscribe({
          next: result => {
            this.carregando = false;
            this.router.navigate(['/session/signin', { savedRoute: this.savedRoute }]);
          },
          error: error => {
            this.carregando = false;
          }
        });
    }
  }

  goToLogin(){
    this.router.navigate(['session/signin', { savedRoute: this.savedRoute }]);
  }
}
