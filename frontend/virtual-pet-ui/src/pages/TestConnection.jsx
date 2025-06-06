import React, { useState, useEffect } from 'react';
import axios from 'axios';

const TestConnection = () => {
    const [tests, setTests] = useState({
        health: { status: 'Testing...', result: null, error: null },
        cors: { status: 'Testing...', result: null, error: null },
        auth: { status: 'Testing...', result: null, error: null }
    });

    useEffect(() => {
        runAllTests();
    }, []);

    const updateTest = (testName, status, result = null, error = null) => {
        setTests(prev => ({
            ...prev,
            [testName]: { status, result, error }
        }));
    };

    const testHealth = async () => {
        updateTest('health', 'Testing...');
        try {
            const response = await axios.get('http://localhost:8088/actuator/health');
            updateTest('health', '✅ Success', response.data);
        } catch (err) {
            updateTest('health', '❌ Failed', null, err.message);
        }
    };

    const testCors = async () => {
        updateTest('cors', 'Testing...');
        try {
            const response = await axios.get('http://localhost:8088/actuator/info');
            updateTest('cors', '✅ CORS Working', response.data);
        } catch (err) {
            if (err.message.includes('CORS')) {
                updateTest('cors', '❌ CORS Issue', null, 'CORS policy blocking request');
            } else {
                updateTest('cors', '⚠️ Other Error', null, err.message);
            }
        }
    };

    const testAuth = async () => {
        updateTest('auth', 'Testing...');
        try {
            const response = await axios.get('http://localhost:8088/actuator/info', {
                auth: {
                    username: 'admin',
                    password: 'admin123'
                }
            });
            updateTest('auth', '✅ Auth Working', response.data);
        } catch (err) {
            updateTest('auth', '❌ Auth Failed', null, err.message);
        }
    };

    const runAllTests = () => {
        testHealth();
        testCors();
        testAuth();
    };

    const TestResult = ({ test, title }) => (
        <div style={{
            padding: '1rem',
            margin: '0.5rem 0',
            background: 'rgba(255,255,255,0.1)',
            borderRadius: '8px',
            border: `1px solid ${test.status.includes('✅') ? '#4CAF50' : test.status.includes('❌') ? '#f44336' : '#ff9800'}`
        }}>
            <h3>{title}</h3>
            <p><strong>Status:</strong> {test.status}</p>
            {test.result && (
                <details>
                    <summary>Response Data</summary>
                    <pre style={{ fontSize: '0.8rem', overflow: 'auto' }}>
            {JSON.stringify(test.result, null, 2)}
          </pre>
                </details>
            )}
            {test.error && (
                <p style={{ color: '#ff6b6b' }}><strong>Error:</strong> {test.error}</p>
            )}
        </div>
    );

    return (
        <div style={{ padding: '2rem', color: 'white', maxWidth: '800px', margin: '0 auto' }}>
            <h1>Backend Connection Test</h1>

            <div style={{
                padding: '1rem',
                background: 'rgba(255,255,255,0.05)',
                borderRadius: '8px',
                marginBottom: '2rem'
            }}>
                <h2>Configuration</h2>
                <p><strong>Frontend:</strong> http://localhost:5173</p>
                <p><strong>Backend:</strong> http://localhost:8088</p>
                <p><strong>Backend Status:</strong> Running</p>
            </div>

            <div style={{ marginBottom: '2rem' }}>
                <button
                    onClick={runAllTests}
                    style={{
                        padding: '0.75rem 1.5rem',
                        background: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontSize: '1rem',
                        marginRight: '1rem'
                    }}
                >
                    Run All Tests
                </button>

                <a
                    href="http://localhost:8088/actuator/health"
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{
                        padding: '0.75rem 1.5rem',
                        background: '#2196F3',
                        color: 'white',
                        textDecoration: 'none',
                        borderRadius: '8px',
                        display: 'inline-block'
                    }}
                >
                    Open Health Check
                </a>
            </div>

            <div>
                <TestResult test={tests.health} title="1. Health Check (No Auth)" />
                <TestResult test={tests.cors} title="2. CORS Test" />
                <TestResult test={tests.auth} title="3. Authentication Test" />
            </div>
        </div>
    );
};

export default TestConnection;