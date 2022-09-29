import { SharedModule } from './../core/shared/shared.module';
import { LandingComponent } from './landing/landing.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutes } from './main.routing';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';


@NgModule({
  declarations: [LandingComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(MainRoutes),
    SharedModule,
    MatButtonModule,
    MatButtonToggleModule,
    FormsModule,
    MatGridListModule,
    MatProgressSpinnerModule,
  ]
})
export class MainModule { }
