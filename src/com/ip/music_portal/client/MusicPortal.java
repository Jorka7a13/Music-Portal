package com.ip.music_portal.client;

import java.util.List;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ip.music_portal.client.User.UserStatus;
import com.ip.music_portal.shared.LoginVerifier;

public class MusicPortal implements EntryPoint {
	
	static IUserManagerAsync userManagerService = (IUserManagerAsync) GWT.create(IUserManager.class);
	
	static IUploadManagerAsync uploadManagerService = (IUploadManagerAsync) GWT.create(IUploadManager.class);
	
	static ICommentManagerAsync commentManagerService = (ICommentManagerAsync) GWT.create(ICommentManager.class);
	
	static User user;
	
	static boolean isInputValid = false;
	
	static boolean hasAdminPanelBeingShown = false;
	
	static boolean soundIsPlaying = false;
	
	static boolean hasCommentsBeingShown = false;
	
	static SoundController soundController = new SoundController();
	
	static Sound sound;
	
	static VerticalPanel commentsVPanel;
	
	private static class PlayerGUI {
		
		private static ListBox uploadsListBox;
		private static Button playButton;
		private static Button stopButton;
		private static HTML uploaderLabel;
		private static HTML uploaderNameLabel;
		private static HTML commentsLabel;
		private static Button addComment;
		
		public PlayerGUI() {			
		
			uploadsListBox = new ListBox();
			uploadsListBox.setVisibleItemCount(20);
			uploadsListBox.setHeight("400px");
			uploadsListBox.setWidth("350px");
			uploadsListBox.setStyleName("UploadListStyle");
			uploadsListBox.setTitle("Choose Song ...");
			
			uploaderLabel = new HTML();
			uploaderLabel.setText("Uploader:");
			
			uploaderNameLabel = new HTML();
			commentsLabel = new HTML("Comments:");
			
			playButton = new Button("Play");
			stopButton = new Button("Stop");
			playButton.setSize("80px", "30px");
			stopButton.setSize("80px", "30px");
			playButton.setStyleName("Button");
			stopButton.setStyleName("Button");
			addComment = new Button("Add Comment");
			addComment.setSize("115px", "30px");
			addComment.setStyleName("Button");
			
			commentsVPanel = new VerticalPanel();
			
			uploadManagerService.getAllUploadNames(new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					uploadsListBox.addItem("Error in loading tracks!");
				}

				@Override
				public void onSuccess(List<String> result) {
					for(String currentName : result) {
						uploadsListBox.addItem(currentName);
					}
				}
				
			});
			
			playButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
				
					if(uploadsListBox.getSelectedIndex() != -1) {
					
						if(soundIsPlaying) {
							sound.stop();
							soundIsPlaying = false;
						}
						
						hasCommentsBeingShown = true;
						
						String uploadName = uploadsListBox.getItemText(uploadsListBox.getSelectedIndex());
						
						uploadManagerService.getUploadByName(uploadName, new AsyncCallback<Upload>() {
	
							@Override
							public void onFailure(Throwable caught) {
								System.out.println("RPC failed!");
							}
	
							@Override
							public void onSuccess(Upload result) {
								
								uploaderNameLabel.setText(result.getUploaderName());
								
								sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG_MP3, result.getUrlToFile());
			
								sound.addEventHandler(new SoundHandler() {
									
									public void onPlaybackComplete(PlaybackCompleteEvent event) {}
									
									public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
										sound.play();	
										soundIsPlaying = true;
									}			
								});
								
							}
						});
						
						commentManagerService.getAllComments(uploadsListBox.getItemText(uploadsListBox.getSelectedIndex()), new AsyncCallback<List<Comment>>() {
							
							@Override
							public void onFailure(Throwable caught) {
								System.out.println("Error in loading comments!");
							}
			
							@Override
							public void onSuccess(List<Comment> result) {
								
								if(hasCommentsBeingShown) {
									commentsVPanel.clear();
								} 
								
								for(final Comment currentComment : result) {
									final TextArea textArea = new TextArea();
									textArea.setReadOnly(true);
									textArea.setText(currentComment.getText());
									
									final HorizontalPanel commentHPanel = new HorizontalPanel();
									commentHPanel.setSize("100px", "60px");
									commentHPanel.add(textArea);	
									
									Button deleteCommentButton;
									deleteCommentButton = new Button("X");
									deleteCommentButton.setStyleName("Button");
									
									if(user.isLoggedIn()) {
										if(user.getUserStatus().equals(UserStatus.REGULAR)) { // If the user is not admin, let him delete only his comments
											if(currentComment.getAuthor().equals(user.getUserName())) { // If you are the author, you can delete your comment
						
												deleteCommentButton.addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent event) {
		
														commentManagerService.deleteComment(currentComment, new AsyncCallback<Boolean>() {
		
															@Override
															public void onFailure(Throwable caught) {													
															}
		
															@Override
															public void onSuccess(Boolean result) {	
																
																if(result) {															
																	commentsVPanel.remove(commentHPanel);
																} 
																
															}
														});
														
													}
												});	
												
												commentHPanel.add(deleteCommentButton);
											}
										} else { // If the user is admin, let him delete all comments
											
											deleteCommentButton.addClickHandler(new ClickHandler() {
												
												@Override
												public void onClick(ClickEvent event) {
	
													commentManagerService.deleteComment(currentComment, new AsyncCallback<Boolean>() {
	
														@Override
														public void onFailure(Throwable caught) {													
														}
	
														@Override
														public void onSuccess(Boolean result) {	
															
															if(result) {															
																commentsVPanel.remove(commentHPanel);
															} 
															
														}
													});
													
												}
											});
											
											commentHPanel.add(deleteCommentButton);
										}
									}
									
									commentsVPanel.add(commentHPanel);
								}	
								
							}
						});
						
					}		
				}
			});
			
			stopButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {				
					if(soundIsPlaying) {
						sound.stop();
					}									
				}
			});
			
			addComment.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new AddCommentDialogBox();
				}
			});
			
		}
		
		private static class AddCommentDialogBox extends DialogBox {

			public AddCommentDialogBox() {
				// Set the dialog box's caption.
				setText("Add Comment");

				VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setStyleName("addCommentVerticalPanel");
				verticalPanel.setPixelSize(250, 100);
				
				HorizontalPanel textHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

		        final TextArea commentTextArea = new TextArea();   
				Button addCommentButton = new Button("Add Comment");
				Button cancelButton = new Button("Cancel");
				addCommentButton.setStyleName("Button");
				cancelButton.setStyleName("Button");

				textHorizontalPanel.add(commentTextArea);
				commentTextArea.setSize("300px", "300px");
				
				buttonHorizontalPanel.add(addCommentButton);
				addCommentButton.setSize("125px","30px");
				buttonHorizontalPanel.add(cancelButton);
				cancelButton.setSize("125px","30px");

				verticalPanel.add(textHorizontalPanel);
				verticalPanel.add(buttonHorizontalPanel);

				addCommentButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						String text = commentTextArea.getText();
						String author = user.getUserName();
						String uploadName = uploadsListBox.getItemText(uploadsListBox.getSelectedIndex());;
						
						final Comment comment = new Comment(uploadName, author, text);
						
						commentManagerService.addComment(comment, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								new MessagePopup("Adding of comment failed!!");
							}

							@Override
							public void onSuccess(Void result) {
								AddCommentDialogBox.this.hide();
								
								TextArea textArea = new TextArea();
								textArea.setReadOnly(true);
								textArea.setText(comment.getText());
								
								new MessagePopup("Comment added!");
							}
						});
	
					}
				});
				
				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						AddCommentDialogBox.this.hide(); // Hides the Comment Dialog Box
					}
				});

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				setWidget(verticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();
			}
	
		}

		public void showPlayerGUI() {
			RootPanel.get().add(uploadsListBox);
			RootPanel.get().add(playButton);
			RootPanel.get().add(stopButton);
			RootPanel.get().add(uploaderLabel);
			RootPanel.get().add(uploaderNameLabel);
			RootPanel.get().add(commentsLabel);
			RootPanel.get().add(addComment);
			RootPanel.get().add(commentsVPanel);
				
			RootPanel.get().setWidgetPosition(uploadsListBox, 400, 150);
			RootPanel.get().setWidgetPosition(playButton, 400, 555);
			RootPanel.get().setWidgetPosition(stopButton, 485, 555);
			RootPanel.get().setWidgetPosition(uploaderLabel, 755, 150);
			RootPanel.get().setWidgetPosition(uploaderNameLabel, 815, 150);
			RootPanel.get().setWidgetPosition(commentsLabel, 755, 170);
			RootPanel.get().setWidgetPosition(addComment, 570, 555);
			RootPanel.get().setWidgetPosition(commentsVPanel, 758, 190);
		}
		
		public static void addTrackToUploadsList(String name) {
			uploadsListBox.addItem(name);
		}
		
		public static void removeTrackFromUploadsList(String name) {
				
			for(int index = 0; index < uploadsListBox.getItemCount(); index++) {
				if(uploadsListBox.getItemText(index).equals(name)) {
					uploadsListBox.removeItem(index);
				}
			}
		}
		
	}
		
	private static class AdminGUI {
		
		static Button showAdminPanelButton;
		static Button hideAdminPanelButton;
	
		public AdminGUI() {
			
			showAdminPanelButton = new Button("Show Admin Panel");
			showAdminPanelButton.setSize("140px", "30px");	
			showAdminPanelButton.setStyleName("Button");
			
			showAdminPanelButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new AdminPanelGUI();
					AdminPanelGUI.showAdminPanelGUI();
					RootPanel.get().remove(showAdminPanelButton);
					hideAdminPanelButton = new Button("Hide Admin Panel");
					hideAdminPanelButton.setSize("140px", "30px");	
					hideAdminPanelButton.setStyleName("Button");
					RootPanel.get().add(hideAdminPanelButton);
					hasAdminPanelBeingShown = true;
					
					hideAdminPanelButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							AdminPanelGUI.hideAdminPanelGUI();
							RootPanel.get().remove(hideAdminPanelButton);
							RootPanel.get().add(showAdminPanelButton);
						}
					});
	
				}
			});
			
		}
		
		public static void showAdminGUI() {
			RootPanel.get().add(showAdminPanelButton);
		}
		
		public static void hideAdminGUI() {
			if((user.getUserStatus().equals(UserStatus.ADMIN)) && (hasAdminPanelBeingShown)) {
				RootPanel.get().remove(hideAdminPanelButton);
				RootPanel.get().remove(showAdminPanelButton);
				AdminPanelGUI.hideAdminPanelGUI();
			}
		}
		
	}
			 
	
	private static class AdminPanelGUI {
		 
		static Button showAllUsers;
		static Button showAllUploads;
		
		public AdminPanelGUI() {
			
			showAllUsers = new Button("Show All Users");
			showAllUsers.setSize("120px", "30px");
			showAllUsers.setStyleName("Button");
			showAllUploads= new Button("Show All Uploads");
			showAllUploads.setSize("140px", "30px");
			showAllUploads.setStyleName("Button");
			
			showAllUsers.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {					
					new ShowAllUsersDialogBox();					
				}
			});
			
			showAllUploads.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new ShowAllUploadsDialogBox();
				}
			});
				
		} 
		
		private static class ShowAllUploadsDialogBox extends DialogBox {
			
			ListBox uploadsListBox;
			
			
			public ShowAllUploadsDialogBox() {
				setText("All Uploads");
				
				VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setPixelSize(250, 80);
				
				HorizontalPanel uploadsHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

				uploadsListBox = new ListBox();
				uploadsListBox.setVisibleItemCount(20);
				uploadsListBox.setSize("200px", "70px");
				uploadsListBox.setStyleName("UploadListStyle");
				
				Button deleteUploadButton = new Button("Delete Track");
				deleteUploadButton.setSize("120px", "30px");
				deleteUploadButton.setStyleName("Button");
				
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName("Button");
				
				uploadManagerService.getAllUploadNames(new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						uploadsListBox.addItem("Error in getting tracks!");
					}

					@Override
					public void onSuccess(List<String> result) {
						for(String currentUpload : result) {
							uploadsListBox.addItem(currentUpload);
						}
					}
				});
				
				uploadsHorizontalPanel.add(uploadsListBox);
				uploadsHorizontalPanel.add(deleteUploadButton);
				
				buttonHorizontalPanel.add(cancelButton);
				cancelButton.setSize("125px","30px");

				verticalPanel.add(uploadsHorizontalPanel);
				verticalPanel.add(buttonHorizontalPanel);
				
				deleteUploadButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {			
						
						if(uploadsListBox.getSelectedIndex() == -1) { // If no track is selected
							new MessagePopup("Please select a track!");
						} else {
							
							final String uploadName = uploadsListBox.getItemText(uploadsListBox.getSelectedIndex());
							
							uploadManagerService.deleteUpload(uploadName, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									new MessagePopup("An internal error has occured! The track has not been deleted!");
								}

								@Override
								public void onSuccess(Boolean result) {
									if(result) {

										for(int index = 0; index < uploadsListBox.getItemCount(); index++) {
											if(uploadsListBox.getItemText(index).equals(uploadName)) {
												uploadsListBox.removeItem(index);
											}
										}
										ShowAllUploadsDialogBox.this.hide();
										PlayerGUI.removeTrackFromUploadsList(uploadName);
										new MessagePopup("The track has been deleted!");
									} else {
										new MessagePopup("There are no tracks with that name!");
									}
								}
								
							});							
						}
						
						uploadsListBox.setSelectedIndex(-1); // Deselects after the track is deleted
						
					}
				});
				
				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						ShowAllUploadsDialogBox.this.hide(); // Hides the "Are You Shure" Dialog Box
					}
				});

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				setWidget(verticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();		
				
			}
			
		}
		
		private static class ShowAllUsersDialogBox extends DialogBox {
			
			ListBox usersListBox;
			
			public ShowAllUsersDialogBox() {
				setText("All Users");

				VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setPixelSize(250, 80);
				
				HorizontalPanel usersHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

				usersListBox = new ListBox();
				usersListBox.setVisibleItemCount(20);
				usersListBox.setSize("200px", "70px");
				usersListBox.setStyleName("UploadListStyle");
				
				Button deleteUserButton = new Button("Delete User");
				deleteUserButton.setSize("120px", "30px");
				deleteUserButton.setStyleName("Button");
				
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName("Button");
				
				userManagerService.getAllUserNames(new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						usersListBox.addItem("Error in loading users!");
					}

					@Override
					public void onSuccess(List<String> result) {
						for(String userName : result) {
							usersListBox.addItem(userName);
						}
					}
				});
				
				usersHorizontalPanel.add(usersListBox);
				usersHorizontalPanel.add(deleteUserButton);
				
				buttonHorizontalPanel.add(cancelButton);
				cancelButton.setSize("125px","30px");

				verticalPanel.add(usersHorizontalPanel);
				verticalPanel.add(buttonHorizontalPanel);
				
				deleteUserButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						if(usersListBox.getSelectedIndex() == -1) { // If nothing is selected 
							new MessagePopup("Please select an user!");
						} else {
							if(usersListBox.getItemText(usersListBox.getSelectedIndex()).equals(user.getUserName())) {
								new AreYouShureDialogBox(usersListBox.getItemText(usersListBox.getSelectedIndex()));							
							} else {
								userManagerService.deleteUser(usersListBox.getItemText(usersListBox.getSelectedIndex()), new AsyncCallback<Boolean>() {
									
									@Override
									public void onFailure(Throwable caught) {
										System.out.println("Failiure in RPC!!!");
									}
			
									@Override
									public void onSuccess(Boolean result) {
										System.out.println("Success in RPC!");
										if(result) {
											ShowAllUsersDialogBox.this.hide();
											new MessagePopup("The user has been deleted!");									
										} else {
											new MessagePopup("There is no such user!");
										}
									}
								});
								usersListBox.setSelectedIndex(-1); // Deselects after the user is deleted
							}
						}
					}
				});	
				
				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						ShowAllUsersDialogBox.this.hide(); // Hides the "Are You Sure" Dialog Box
					}
				});

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				setWidget(verticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();		
				
			}
			
		}
		
		private static class AreYouShureDialogBox extends DialogBox {

			
			public AreYouShureDialogBox(final String userName) {
				// Set the dialog box's caption.
				setText("Delete user");

				VerticalPanel areYouShureVerticalPanel = new VerticalPanel();
				areYouShureVerticalPanel.setStyleName("areYouShureVerticalPanel");
				areYouShureVerticalPanel.setPixelSize(250, 80);
				
				HorizontalPanel captionHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

				Button yesButton = new Button("Yes");
				Button noButton = new Button("No");
				
				HTML captionLabel = new HTML();	
				captionLabel.setText("Are you shure you want to delete your accaunt?");
				
				captionHorizontalPanel.add(captionLabel);
				
				buttonHorizontalPanel.add(yesButton);
				yesButton.setSize("125px","30px");
				buttonHorizontalPanel.add(noButton);
				noButton.setSize("125px","30px");

				areYouShureVerticalPanel.add(captionHorizontalPanel);
				areYouShureVerticalPanel.add(buttonHorizontalPanel);
				
				noButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						AreYouShureDialogBox.this.hide(); // Hides the "Are You Shure" Dialog Box
					}
				});
				
				yesButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						userManagerService.deleteUser(userName, new AsyncCallback<Boolean>() {
							
							@Override
							public void onFailure(Throwable caught) {
								System.out.println("Failiure in RPC!!!");
							}
	
							@Override
							public void onSuccess(Boolean result) {
								System.out.println("Success in RPC!");
								if(result) {
									new MessagePopup("The user has been deleted!");
								} else {
									new MessagePopup("There is no such user!");
								}
							}
						});
						AreYouShureDialogBox.this.hide();	
						user.setLoggedIn(false);						
						LoginGUI.hideLogoutGUI();
						LoginGUI.showLoginGUI();
						AdminGUI.hideAdminGUI();
						UploadGUI.hideUploadGUI();
					}
				});

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				setWidget(areYouShureVerticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();
			}
		
		}
		
		public static void showAdminPanelGUI() {
			RootPanel.get().add(showAllUsers);
			RootPanel.get().add(showAllUploads);
		}
		
		public static void hideAdminPanelGUI() {
			RootPanel.get().remove(showAllUsers);
			RootPanel.get().remove(showAllUploads);
		}
		
	} 
	
	private static class LoginGUI {
		
		static Button loginButton;
		static Button registerButton;
		static Button logoutButton;
		
		public LoginGUI() {
			loginButton = new Button("Login");
			registerButton = new Button("Register");
			logoutButton = new Button("Logout");
			loginButton.setSize("80px", "30px");
			registerButton.setSize("80px", "30px");
			logoutButton.setSize("80px", "30px");	
			
			loginButton.setStyleName("Button");
			logoutButton.setStyleName("Button");
			registerButton.setStyleName("Button");
		}
		
		public static void showLoginGUI() {
			RootPanel.get().add(loginButton);
			RootPanel.get().add(registerButton);
			
			RootPanel.get().setWidgetPosition(loginButton, 1000, 75);
			RootPanel.get().setWidgetPosition(registerButton, 1080, 75);
						
		}
		
		public static void hideLoginGUI() {
			RootPanel.get().remove(loginButton);
			RootPanel.get().remove(registerButton);
		}
		
		public static void showLogoutGUI() {
			RootPanel.get().add(logoutButton);
			RootPanel.get().setWidgetPosition(logoutButton, 1000, 75);
		}
		
		public static void hideLogoutGUI() {
			RootPanel.get().remove(logoutButton);
		}
		
		private static class RegisterDialogBox extends DialogBox {

			String userName;
			String password;
			String repeatPassword;
			String email;
			
			public RegisterDialogBox() {
				setText("Please Register");

				VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setStyleName("registerVerticalPanel");
				verticalPanel.setPixelSize(250, 280);

				HorizontalPanel userNameHorizontalPanel = new HorizontalPanel();

				HorizontalPanel passwordHorizontalPanel = new HorizontalPanel();
				HorizontalPanel repeatPasswordHorizontalPanel = new HorizontalPanel();
				HorizontalPanel emailHorizontalPanel = new HorizontalPanel();
				HorizontalPanel bioHorizontalPanel = new HorizontalPanel();
				HorizontalPanel statusHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();			

			    final TextBox userNameTextBox = new TextBox();		
			    final PasswordTextBox passwordTextBox = new PasswordTextBox();
			    final PasswordTextBox repeatPasswordTextBox = new PasswordTextBox();	
			    final TextBox emailTextBox = new TextBox();	
			    final TextArea bioTextArea = new TextArea();
			    final CheckBox statusCheckBox = new CheckBox();
				Button registerButton = new Button("Register");
				Button closeButton = new Button("Close");
				registerButton.setStyleName("Button");
				closeButton.setStyleName("Button");
				
				HTML labelUserName = new HTML();
				HTML labelPassword = new HTML();
				HTML labelRepeatPass = new HTML();
				HTML labelEmail = new HTML();
				HTML labelBio = new HTML();
				HTML labelStatus = new HTML();
				
				labelUserName.setText("User Name:");
				labelUserName.setSize("90px", "15px");
				userNameHorizontalPanel.add(labelUserName);
				
				userNameHorizontalPanel.add(userNameTextBox);
				userNameTextBox.setWidth("160px");
				
				labelPassword.setText("Password:");
				labelPassword.setSize("90px", "15px");
				passwordHorizontalPanel.add(labelPassword);
				
				passwordHorizontalPanel.add(passwordTextBox);
				passwordTextBox.setWidth("160px");
						
				labelRepeatPass.setText("Repeat Password:");
				labelRepeatPass.setSize("90px", "15px");
				repeatPasswordHorizontalPanel.add(labelRepeatPass);
				
				repeatPasswordHorizontalPanel.add(repeatPasswordTextBox);
				repeatPasswordTextBox.setWidth("160px");
				
				labelEmail.setText("E-mail:");
				labelEmail.setSize("90px", "15px");
				emailHorizontalPanel.add(labelEmail);
	 
				emailHorizontalPanel.add(emailTextBox);
				emailTextBox.setWidth("160px");
				
				labelBio.setText("*Bio:");
				labelBio.setSize("90px", "15px");
				bioHorizontalPanel.add(labelBio);
				
				bioHorizontalPanel.add(bioTextArea);
				bioTextArea.setWidth("160px");
				
				labelStatus.setText("Admin:");
				labelStatus.setSize("90px", "15px");
				statusHorizontalPanel.add(labelStatus);
				
				statusHorizontalPanel.add(statusCheckBox);
				
				registerButton.setSize("125px", "30px");
				buttonHorizontalPanel.add(registerButton);
				closeButton.setSize("125px", "30px");
				buttonHorizontalPanel.add(closeButton);

				verticalPanel.add(userNameHorizontalPanel);
				verticalPanel.add(passwordHorizontalPanel);
				verticalPanel.add(repeatPasswordHorizontalPanel);
				verticalPanel.add(emailHorizontalPanel);
				verticalPanel.add(bioHorizontalPanel);
				verticalPanel.add(statusHorizontalPanel);
				verticalPanel.add(buttonHorizontalPanel);

				registerButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// Register the user
						userName = userNameTextBox.getText();
						password = passwordTextBox.getText();
						repeatPassword = repeatPasswordTextBox.getText();
						email = emailTextBox.getText();
						
						if(!LoginVerifier.isUserNameValid(userName)) {
							new MessagePopup("Invalid user name!");
						} else if(!LoginVerifier.isPasswordValid(password)) {
							new MessagePopup("Invalid password!");
						} else if(!LoginVerifier.isPasswordValid(repeatPassword)) {
							new MessagePopup("Invalid repeated password!");
						} else if(!LoginVerifier.isEmailValid(email)) {
							new MessagePopup("Invalid email!");
						} else {
							isInputValid = true;
						}
						
						ServiceDefTarget userManagerServiceDef = (ServiceDefTarget) userManagerService;
						userManagerServiceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "userManager");
						
						ServiceDefTarget uploadManagerServiceDef = (ServiceDefTarget) uploadManagerService;
						uploadManagerServiceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "uploadManager");
						
						if(isInputValid) { // If the input has no blanks, the length is valid and the email is valid too
						
							userManagerService.containsUserWithUserName(userName, new AsyncCallback<Boolean>() { // Make RPC for checking if there is such user
	
								@Override
								public void onFailure(Throwable caught) { // If RPC fails
									Window.alert("Failure due: " + caught.getMessage());
								}
	
								@Override
								public void onSuccess(Boolean result) { // If RPC doesn't fail
					
									if(!result) { // If there is no user with that user name
										if(password.equals(repeatPassword)) { // If the password matches	
						
											user = new User(userName, password, email);
											
											if(bioTextArea.getText() != null) {
												user.setBio(bioTextArea.getText()); 
											}
											
											if(statusCheckBox.isChecked()) {
												user.setUserStatus(UserStatus.ADMIN);
											}
											
											userManagerService.addUser(user, new AsyncCallback<Boolean>() { // Make RPC for adding the user
	
												public void onFailure(Throwable caught) { // If RPC fails
													Window.alert("Failure due: " + caught.getMessage());
												}
	
												@Override
												public void onSuccess(Boolean result) { // If RPC doesn't fail	
													System.out.println("The RPC for the add user was successfull!");
													
													if(result) {
														new MessagePopup("You have registered successfully!");
													} else {
														new MessagePopup("That username is already taken!");
													}
												}
												
											}); 
											
											isInputValid = false;
											RegisterDialogBox.this.hide();

										} else {
											new MessagePopup("Password doesn't match!");
										}
									} else {
										new MessagePopup("This username is already taken!");
									}
								}
								
							});	
						}
					}
				});

				closeButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						RegisterDialogBox.this.hide(); // Hides the Register Dialog Box													
					}
				});

				setWidget(verticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();
			}
		}
		
		private static class LoginDialogBox extends DialogBox {
			
			String userName;
			String password;
			
			public LoginDialogBox() {
				// Set the dialog box's caption.
				setText("Please Login");

				VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setStyleName("loginVerticalPanel");
				verticalPanel.setPixelSize(250, 100);
				
				HorizontalPanel userNameHorizontalPanel = new HorizontalPanel();
				HorizontalPanel passwordHorizontalPanel = new HorizontalPanel();
				HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

		        final TextBox userNameTextBox = new TextBox();   
		        final PasswordTextBox passwordTextBox = new PasswordTextBox(); 
				Button loginButton = new Button("Login");
				Button cancelButton = new Button("Cancel");
				loginButton.setStyleName("Button");
				cancelButton.setStyleName("Button");
				
				HTML labelUserName = new HTML();
				HTML labelPassword = new HTML();		
				
				labelUserName.setText("User Name:");
				labelUserName.setSize("90px", "15px");
				userNameHorizontalPanel.add(labelUserName);
				userNameHorizontalPanel.add(userNameTextBox);
				userNameTextBox.setWidth("160px");
				
				labelPassword.setText("Password:");
				labelPassword.setSize("90px", "15px");
				passwordHorizontalPanel.add(labelPassword);
				passwordHorizontalPanel.add(passwordTextBox);
				passwordTextBox.setWidth("160px");
				
				buttonHorizontalPanel.add(loginButton);
				loginButton.setSize("125px","30px");
				buttonHorizontalPanel.add(cancelButton);
				cancelButton.setSize("125px","30px");

				verticalPanel.add(userNameHorizontalPanel);
				verticalPanel.add(passwordHorizontalPanel);
				verticalPanel.add(buttonHorizontalPanel);
				
				loginButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
					// Makes the login stuff here
						
						userName = userNameTextBox.getText();
					
						userManagerService.containsUserWithUserName(userName, new AsyncCallback<Boolean>() { // Make RPC to log the user in
	
							@Override
							public void onFailure(Throwable caught) { // If RPC fails
								Window.alert("Failure due: " + caught.getMessage());
							}
	
							@Override
							public void onSuccess(Boolean result) { // If RPC doesn't fail
								
								if(result) { // If the user exists
									password = passwordTextBox.getText();
									userManagerService.getUserByUserName(userName, new AsyncCallback<User>() {
	
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Failure due: " + caught.getMessage());
										}
	
										@Override
										public void onSuccess(User result) {
											user = result;
											
											if(user.getPassword().equals(password)) { // If the password matches
												user.setLoggedIn(true); // Logs the user in
												LoginDialogBox.this.hide();		
												hideLoginGUI();
												showLogoutGUI();
												new UploadGUI();
												UploadGUI.showUploadGUI();	
												
												if(user.getUserStatus().equals(UserStatus.ADMIN)) {
													new AdminGUI();
													AdminGUI.showAdminGUI();
												} 

												new MessagePopup("You have logged in!");
											} else {
												new MessagePopup("Wrong password!");
											}
										}
										
									});
									
								} else {
									new MessagePopup("Wrong username!");
								}								
							}							
						});		
					}
				});

				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						LoginDialogBox.this.hide(); // Hides the Login Dialog Box
					}
				});

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				setWidget(verticalPanel);

				setGlassEnabled(true); // Make the background gray
				setAnimationEnabled(true); // Add animation
				center();
			}
		}
		
	}

	private static class UploadGUI {
		
		static Button uploadButton;
		
		public UploadGUI() {
			uploadButton = new Button();
			uploadButton.setText("Upload");
			uploadButton.setSize("100px", "30px");
			uploadButton.setStyleName("Button");
			
			uploadButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// Show the upload popup panel				
					new UploadDialogBox();	
				}
			});
			
		}
			
			private static class UploadDialogBox extends DialogBox {
				
				Upload upload;
				
				public UploadDialogBox() {
					// Set the dialog box's caption.
					setText("Upload a song!");

					VerticalPanel verticalPanel = new VerticalPanel();
					verticalPanel.setStyleName("uploadVerticalPanel");
					verticalPanel.setPixelSize(250, 100);
					
					HorizontalPanel nameHorizontalPanel = new HorizontalPanel();
					HorizontalPanel urlHorizontalPanel = new HorizontalPanel();
					HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();

			        final TextBox nameTextBox = new TextBox(); 
			        final TextBox urlTextBox = new TextBox(); 
					Button uploadButton = new Button("Upload");
					Button cancelButton = new Button("Cancel");
					uploadButton.setStyleName("Button");
					cancelButton.setStyleName("Button");
					
					HTML nameLabel = new HTML();
					HTML urlLabel = new HTML();		
					
					nameLabel.setText("Name:");
					nameLabel.setSize("90px", "15px");
					nameHorizontalPanel.add(nameLabel);
					nameHorizontalPanel.add(nameTextBox);
					nameTextBox.setWidth("160px");
					
					urlLabel.setText("URL to file:");
					urlLabel.setSize("90px", "15px");
					urlHorizontalPanel.add(urlLabel);
					urlHorizontalPanel.add(urlTextBox);
					urlTextBox.setWidth("160px");
					
					buttonHorizontalPanel.add(uploadButton);
					uploadButton.setSize("125px","30px");
					buttonHorizontalPanel.add(cancelButton);
					cancelButton.setSize("125px","30px");

					verticalPanel.add(nameHorizontalPanel);
					verticalPanel.add(urlHorizontalPanel);
					verticalPanel.add(buttonHorizontalPanel);
					
					
					
					cancelButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							UploadDialogBox.this.hide(); // Hides the Upload Dialog Box
						}
					});
					
					uploadButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							
							upload = new Upload(nameTextBox.getText(), user.getUserName(), urlTextBox.getText());
							
							uploadManagerService.addUpload(upload, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									new MessagePopup("Error in uploading!");
								}

								@Override
								public void onSuccess(Void result) {
									new MessagePopup("You have uploaded the song successfully!");
								}
							});
							
							UploadDialogBox.this.hide();
							PlayerGUI.addTrackToUploadsList(nameTextBox.getText());						
						}
					});

					// DialogBox is a SimplePanel, so you have to set its widget
					// property to
					// whatever you want its contents to be.
					setWidget(verticalPanel);

					setGlassEnabled(true); // Make the background gray
					setAnimationEnabled(true); // Add animation
					center();
				}
			
			}
		
		public static void showUploadGUI() {
			RootPanel.get().add(uploadButton);
			RootPanel.get().setWidgetPosition(uploadButton, 1080, 75);
		}
		
		public static void hideUploadGUI() {
			RootPanel.get().remove(uploadButton);
		}
		
	}
	 
	@Override
	public void onModuleLoad() {

		RootPanel.get().setStyleName("backgroundImage");
		
		new PlayerGUI().showPlayerGUI();
		
		new LoginGUI();	
		LoginGUI.showLoginGUI();
		
		LoginGUI.loginButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				
				LoginGUI.LoginDialogBox loginDialogBox = new LoginGUI.LoginDialogBox();
				loginDialogBox.show();	
					
				LoginGUI.logoutButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						LoginGUI.hideLogoutGUI();
						LoginGUI.showLoginGUI();				
						AdminGUI.hideAdminGUI();
						UploadGUI.hideUploadGUI();
						user.setLoggedIn(false);
						new MessagePopup("You have logged out!");
					}
				});
									
			}
		});
		
		LoginGUI.registerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new LoginGUI.RegisterDialogBox().show();
			}
		});
		
	}
	
	// Some utility classes
	
	public static class MessagePopup extends PopupPanel { // Takes a string and creates a popup panel with it

	    public MessagePopup(String message) {
	      // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
	      // If this is set, the panel closes itself automatically when the user
	      // clicks outside of it.
	      super(true);

	      // PopupPanel is a SimplePanel, so you have to set it's widget property to
	      // whatever you want its contents to be.
	      setWidget(new Label(message));     	
	     
		  setGlassEnabled(true); // Make the background gray
		  setAnimationEnabled(true); // Add animation
	      
	      center();
	    }
	}
	
}
