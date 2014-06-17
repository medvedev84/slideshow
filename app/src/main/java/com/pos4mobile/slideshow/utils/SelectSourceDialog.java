package com.pos4mobile.slideshow.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectSourceDialog {
    private static final String PARENT_DIR = "..";
    private String[] fileList;
    private File currentPath;

    public interface FileSelectedListener {
        void fileSelected(File file);
    }

    public interface DirectorySelectedListener {
        void directorySelected(File directory);
    }

    private ListenerList<FileSelectedListener> fileListenerList = new ListenerList<SelectSourceDialog.FileSelectedListener>();
    private ListenerList<DirectorySelectedListener> dirListenerList = new ListenerList<SelectSourceDialog.DirectorySelectedListener>();
    private final Activity activity;
    private boolean selectDirectoryOption;
    private String fileEndsWith;

    /**
     * @param activity
     * @param path
     */
    public SelectSourceDialog(Activity activity, File path) {
        this.activity = activity;
        if (!path.exists()) path = Environment.getExternalStorageDirectory();
        loadFileList(path);
    }

    /**
     * @return file dialog
     */
    public Dialog createFileDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption) {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    fireDirectorySelectedEvent(currentPath);
                }
            });
        }
        builder.setMultiChoiceItems(fileList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {

            }
        });


        /*
        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog();
                } else fireFileSelectedEvent(chosenFile);
            }
        });
        */

        
        dialog = builder.show();
        return dialog;
    }


    public SelectSourceDialog addFileListener(FileSelectedListener listener) {
        fileListenerList.add(listener);
        return this;
    }

    public SelectSourceDialog removeFileListener(FileSelectedListener listener) {
        fileListenerList.remove(listener);
        return this;
    }

    public void setSelectDirectoryOption(boolean selectDirectoryOption) {
        this.selectDirectoryOption = selectDirectoryOption;
    }

    public void addDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.add(listener);
    }

    public void removeDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.remove(listener);
    }

    /**
     * Show file dialog
     */
    public SelectSourceDialog showDialog() {
        createFileDialog().show();
        return this;
    }

    private void fireFileSelectedEvent(final File file) {
        fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>() {
            public void fireEvent(FileSelectedListener listener) {
                listener.fileSelected(file);
            }
        });
    }

    private void fireDirectorySelectedEvent(final File directory) {
        dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>() {
            public void fireEvent(DirectorySelectedListener listener) {
                listener.directorySelected(directory);
            }
        });
    }

    private void loadFileList(File path) {
        this.currentPath = path;
        List<String> r = new ArrayList<String>();
        if (path.exists()) {
            if (path.getParentFile() != null) r.add(PARENT_DIR);
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (!sel.canRead()) return false;

                    boolean endsWith = fileEndsWith == null || filename.toLowerCase().endsWith(fileEndsWith);
                    return endsWith || sel.isDirectory();
                    
                    /*
                    if (selectDirectoryOption) 
                    	return sel.isDirectory();
                    else {
                        boolean endsWith = fileEndsWith == null || filename.toLowerCase().endsWith(fileEndsWith);
                        return endsWith || sel.isDirectory();
                    }
                    */
                }
            };
            String[] fileList1 = path.list(filter);
            if (fileList1 != null)
            	Collections.addAll(r, fileList1);
        }
        fileList = r.toArray(new String[r.size()]);
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
        else return new File(currentPath, fileChosen);
    }

    public SelectSourceDialog setFileEndsWith(String fileEndsWith) {
        this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : fileEndsWith;
        return this;
    }
}
