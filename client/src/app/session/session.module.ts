import { UserSettingsComponent } from './user-settings/user-settings.component';
import { HttpClientModule } from '@angular/common/http';
import { SigninComponent } from './signin/signin.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { SessionRoutes } from './session.routing';
import { CreateAccountComponent } from './create-account/create-account.component';
import { SessionService } from './session.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../core/shared/shared.module';
import { AuthService } from '../core/services/auth.service';

@NgModule({
  declarations: [
    CreateAccountComponent,
    SigninComponent,
    UserSettingsComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(SessionRoutes),
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule,
    SharedModule
  ],
  providers:[
    SessionService,
    AuthService
  ]
})
export class UserModule { }
