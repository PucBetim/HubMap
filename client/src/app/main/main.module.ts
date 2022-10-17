import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatMenuModule } from '@angular/material/menu';

import { MainRoutes } from './main.routing';
import { SharedModule } from './../core/shared/shared.module';
import { LandingComponent } from './landing/landing.component';
import { CommentComponent } from './comment/comment.component';
import { PostDetailsComponent } from './post-details/post-details.component';

@NgModule({
  declarations: [LandingComponent, PostDetailsComponent, CommentComponent],
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
    ReactiveFormsModule,
    MatExpansionModule,
    MatMenuModule
  ]
})
export class MainModule { }
