package com.lvg.ssm;

import java.time.LocalDate;

/**
 * Created by Victor Levchenko LVG Corp. on 24.11.2020.
 */
public class JournalWeldingEntity {
    private String number;
    private String objectName;
    private String objectAddress;
    private String customer;
    private String contractNumberProduction;
    private String projectDeveloper;
    private String workingDrawings;
    private String contractNumberDeveloping;
    private String archiveNumberOfWorkingDrawings;
    private LocalDate dateOfStartProduction;
    private LocalDate dateOfEndProduction;
    private String changeNotesOfWorkingDrawings;
    private String timeOfKeepingJournal;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectAddress() {
        return objectAddress;
    }

    public void setObjectAddress(String objectAddress) {
        this.objectAddress = objectAddress;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getContractNumberProduction() {
        return contractNumberProduction;
    }

    public void setContractNumberProduction(String contractNumberProduction) {
        this.contractNumberProduction = contractNumberProduction;
    }

    public String getProjectDeveloper() {
        return projectDeveloper;
    }

    public void setProjectDeveloper(String projectDeveloper) {
        this.projectDeveloper = projectDeveloper;
    }

    public String getWorkingDrawings() {
        return workingDrawings;
    }

    public void setWorkingDrawings(String workingDrawings) {
        this.workingDrawings = workingDrawings;
    }

    public String getContractNumberDeveloping() {
        return contractNumberDeveloping;
    }

    public void setContractNumberDeveloping(String contractNumberDeveloping) {
        this.contractNumberDeveloping = contractNumberDeveloping;
    }

    public String getArchiveNumberOfWorkingDrawings() {
        return archiveNumberOfWorkingDrawings;
    }

    public void setArchiveNumberOfWorkingDrawings(String archiveNumberOfWorkingDrawings) {
        this.archiveNumberOfWorkingDrawings = archiveNumberOfWorkingDrawings;
    }

    public LocalDate getDateOfStartProduction() {
        return dateOfStartProduction;
    }

    public void setDateOfStartProduction(LocalDate dateOfStartProduction) {
        this.dateOfStartProduction = dateOfStartProduction;
    }

    public LocalDate getDateOfEndProduction() {
        return dateOfEndProduction;
    }

    public void setDateOfEndProduction(LocalDate dateOfEndProduction) {
        this.dateOfEndProduction = dateOfEndProduction;
    }

    public String getChangeNotesOfWorkingDrawings() {
        return changeNotesOfWorkingDrawings;
    }

    public void setChangeNotesOfWorkingDrawings(String changeNotesOfWorkingDrawings) {
        this.changeNotesOfWorkingDrawings = changeNotesOfWorkingDrawings;
    }

    public String getTimeOfKeepingJournal() {
        return timeOfKeepingJournal;
    }

    public void setTimeOfKeepingJournal(String timeOfKeepingJournal) {
        this.timeOfKeepingJournal = timeOfKeepingJournal;
    }

    @Override
    public String toString() {
        return  "number='" + number + "\'\t\t\t" +
                "objectName='" + objectName + "\'\t\t\t" +
                "objectAddress='" + objectAddress + "\'\t\t\t" +
                "customer='" + customer + "\'\t\t\t" +
                "contractNumberProduction='" + contractNumberProduction + "\'\t\t\t" +
                "projectDeveloper='" + projectDeveloper + "\'\t\t\t" +
                "workingDrawings='" + workingDrawings + "\'\t\t\t" +
                "contractNumberDeveloping='" + contractNumberDeveloping + "\'\t\t\t" +
                "archiveNumberOfWorkingDrawings='" + archiveNumberOfWorkingDrawings + "\'\t\t\t" +
                "dateOfStartProduction=" + dateOfStartProduction + "\'\t\t\t"+
                "dateOfEndProduction=" + dateOfEndProduction +"\'\t\t\t"+
                "changeNotesOfWorkingDrawings='" + changeNotesOfWorkingDrawings + "\'\t\t\t" +
                "timeOfKeepingJournal='" + timeOfKeepingJournal + '\'';
    }

}
