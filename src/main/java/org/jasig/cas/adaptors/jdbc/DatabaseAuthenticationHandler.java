package org.jasig.cas.adaptors.jdbc;

import java.beans.PropertyVetoException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

public class DatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler implements InitializingBean {

    private static final Configuration configProperties = ConfigProperties.getConfigProperties();
    private static final String SQL_PREFIX = "Select count('x') from ";

    @NotNull
    private String fieldUser;
    @NotNull
    private String fieldPassword;
    @NotNull
    private String fieldLocked;
    @NotNull
    private String tableUsers;

    private String sql;

    public DatabaseAuthenticationHandler() throws PropertyVetoException {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + configProperties.getString("sql.host") + "/" + configProperties.getString("sql.database"));
        ds.setUsername(configProperties.getString("sql.user"));
        ds.setPassword(configProperties.getString("sql.password"));
        ds.setValidationQuery("select 1 from dual"); // Need it for fixing the issue where first login to CAS fails even if the credentials are
                                                     // correct

        super.setDataSource(ds);
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential) throws GeneralSecurityException, PreventedException {
        final String username = credential.getUsername();
        final String encyptedPassword = getPasswordEncoder().encode(credential.getPassword());
        final int count;
        try {
            count = getJdbcTemplate().queryForObject(this.sql, Integer.class, username, encyptedPassword);
        }
        catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        if (count == 0) {
            throw new FailedLoginException(username + " not found with SQL query.");
        }
        return createHandlerResult(credential, new SimplePrincipal(username), null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sql = SQL_PREFIX + this.tableUsers + " Where " + this.fieldUser + " = ? And " + this.fieldPassword + " = ? And " + this.fieldLocked + "= '0'";
    }

    /**
     * @param fieldPassword
     *            The fieldPassword to set.
     */
    public final void setFieldPassword(final String fieldPassword) {
        this.fieldPassword = fieldPassword;
    }

    /**
     * @param fieldLocked
     *            The fieldLocked to set.
     */
    public final void setFieldLocked(final String fieldLocked) {
        this.fieldLocked = fieldLocked;
    }

    /**
     * @param fieldUser
     *            The fieldUser to set.
     */
    public final void setFieldUser(final String fieldUser) {
        this.fieldUser = fieldUser;
    }

    /**
     * @param tableUsers
     *            The tableUsers to set.
     */
    public final void setTableUsers(final String tableUsers) {
        this.tableUsers = tableUsers;
    }

}