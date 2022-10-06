import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PostDetailsComponent } from './post-details/post-details.component';
import { MatDialogModule } from '@angular/material/dialog';

import { MainRoutes } from './main.routing';
import { SharedModule } from './../core/shared/shared.module';
import { LandingComponent } from './landing/landing.component';

@NgModule({
  declarations: [LandingComponent, PostDetailsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(MainRoutes),
    SharedModule,
    MatButtonModule,
    MatButtonToggleModule,
    FormsModule,
    MatGridListModule,
    MatProgressSpinnerModule,
    MatDialogModule,
  ]
})
export class MainModule { }
