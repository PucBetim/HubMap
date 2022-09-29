import { PasswordResetComponent } from './password-reset/password-reset.component';
import { CreateAccountComponent } from './create-account/create-account.component';
import { PageNotFoundComponent } from '../core/page-not-found/page-not-found.component';
import { UserSettingsComponent } from './user-settings/user-settings.component';
import { SigninComponent } from './signin/signin.component';
import { Routes } from '@angular/router';

export const SessionRoutes: Routes = [
  { path: '', component: PageNotFoundComponent },
  { path: 'signin', component: SigninComponent },
  { path: 'create-account', component: CreateAccountComponent },
  //{ path: 'password-reset', component: PasswordResetComponent },
  { path: 'settings', component: UserSettingsComponent }
];
